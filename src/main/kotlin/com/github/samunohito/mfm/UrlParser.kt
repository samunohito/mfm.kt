package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.node.MfmUrl

class UrlParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  companion object {
    private const val recursiveDepthLimit = 20
    private val defaultContext: Context = Context(
      ignoreLinkLabel = false
    )
    private val commaAndPeriodRegex = Regex("[.,]+$")

    private val schemaFinder = RegexFinder(Regex("https?://"))
    private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))
    private val openBracket = StringFinder("(")
    private val closeBracket = StringFinder(")")
    private val openSquareBracket = StringFinder("[")
    private val closeSquareBracket = StringFinder("]")
    private val openRegexBracket = RegexFinder(Regex("([(\\[])"))
    private val closeRegexBracket = RegexFinder(Regex("([)\\]])"))

    private val linkLabelFinders = listOf(
      openSquareBracket,
      wordFinder,
      closeSquareBracket,
      openBracket,
      wordFinder,
      closeBracket,
    )

    private val bracketWrappedFinders = listOf(
      openBracket,
      wordFinder,
      closeBracket,
    )

    private val squareBracketWrappedFinders = listOf(
      openSquareBracket,
      wordFinder,
      closeSquareBracket,
    )

    private val regexBracketWrappedFinders = listOf(
      openRegexBracket,
      wordFinder,
      closeRegexBracket,
    )

    private class UrlFinder(val context: Context) : ISubstringFinder {
      override fun find(input: String, startAt: Int): SubstringFinderResult {
        val inputRange = startAt until input.length
        val text = input.slice(inputRange)
        for (i in text.indices) {
          val result = doFind(text, i)
          if (result.success) {
            return result
          }
        }

        return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, inputRange.last + 1)
      }

      private fun doFind(text: String, startAt: Int): SubstringFinderResult {
        var latestIndex = startAt
        val linkLabelFinderResult = SubstringFinderUtils.sequential(text, latestIndex, linkLabelFinders)
        if (linkLabelFinderResult.success && context.ignoreLinkLabel) {
          // リンク形式として完成している場合はリンクラベル部分を無視してhref部分をチェックしたい
          val labelFinderResult = SubstringFinderUtils.sequential(text, latestIndex, squareBracketWrappedFinders)
          val hrefFinderResult = SubstringFinderUtils.sequential(text, labelFinderResult.next, bracketWrappedFinders)
          if (hrefFinderResult.nests.isNotEmpty()) {
            // 開始カッコだけでも検出できたらURLの開始部分まで一気に飛ばす
            latestIndex = hrefFinderResult.nests[1].range.first
          }
        }

        val schemaResult = schemaFinder.find(text, latestIndex)
        if (schemaResult.success) {
          val urlBodyResult = findUrlBody(text, schemaResult.next)
          if (urlBodyResult.success) {
            val urlRange = schemaResult.range.first..urlBodyResult.range.last
            return SubstringFinderResult.ofSuccess(
              text,
              urlRange,
              urlRange.last + 1,
              listOf(
                schemaResult,
                urlBodyResult,
              )
            )
          }
        }

        return SubstringFinderResult.ofFailure(text, IntRange.EMPTY, -1)
      }

      private fun findUrlBody(text: String, startAt: Int): SubstringFinderResult {
        val results = mutableListOf<SubstringFinderResult>()
        var latestIndex = startAt

        for (depth in 0 until recursiveDepthLimit) {
          val bracketResult = SubstringFinderUtils.sequential(text, latestIndex, regexBracketWrappedFinders)
          if (bracketResult.success) {
            results.add(bracketResult)
            latestIndex = bracketResult.next
            continue
          }

          val wordResult = wordFinder.find(text, latestIndex)
          if (wordResult.success) {
            results.add(wordResult)
            latestIndex = wordResult.next
            continue
          }

          break
        }

        if (results.isEmpty()) {
          return SubstringFinderResult.ofFailure(text, IntRange.EMPTY, -1)
        }

        val firstResult = results.first()
        val lastResult = results.last()
        val resultRange = firstResult.range.first..lastResult.range.last
        return SubstringFinderResult.ofSuccess(text, resultRange, lastResult.next, results)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmUrl> {
    val urlFinder = UrlFinder(context)
    val result = urlFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val proceedResult = processTrailingPeriodAndComma(input, result)
    if (!proceedResult.success) {
      return ParserResult.ofFailure()
    }

    val url = input.substring(proceedResult.range)
    return ParserResult.ofSuccess(MfmUrl(url, false), input, result.range, result.next)
  }

  private fun processTrailingPeriodAndComma(
    input: String,
    finderResult: SubstringFinderResult
  ): SubstringFinderResult {
    val body = finderResult.nests[1]
    val extractUrlBody = input.substring(body.range)
    val matched = commaAndPeriodRegex.find(extractUrlBody)
      ?: // 末尾にピリオドやカンマがない場合はそのまま返す
      return finderResult

    if (extractUrlBody.length - matched.value.length <= 0) {
      // スキーマ形式で始まるが、それ以降がカンマとピリオドのみの場合はスキーマ形式として認識しない
      return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, finderResult.next)
    }

    // finderResultの段階でuntilされているので、ここではやらない（多重にやると範囲がおかしくなる）
    val modifyRange = finderResult.range.first..finderResult.range.last - matched.value.length
    return SubstringFinderResult.ofSuccess(input, modifyRange, finderResult.next)
  }

  data class Context(
    var ignoreLinkLabel: Boolean
  )
}