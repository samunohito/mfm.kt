package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.node.MfmUrl

class UrlParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  companion object {
    private val defaultContext: Context = Context(
      ignoreLinkLabel = false
    )
    private val commaAndPeriodRegex = Regex("[.,]+$")
    private val recursiveDepthLimit = 20

    private val schemaFinder = RegexFinder(Regex("https?://"))
    private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))
    private val openBracket = StringFinder("(")
    private val closeBracket = StringFinder(")")
    private val openSquareBracket = StringFinder("[")
    private val closeSquareBracket = StringFinder("]")

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
        val linkLabelFinderResult = SubstringFinderUtils.sequential(text, startAt, linkLabelFinders)
        if (linkLabelFinderResult.success && context.ignoreLinkLabel) {
          // リンク形式として完成している場合はリンクラベル部分を無視してhref部分をチェックしたい
          val labelFinderResult = SubstringFinderUtils.sequential(text, startAt, squareBracketWrappedFinders)
          val hrefFinderResult = SubstringFinderUtils.sequential(text, labelFinderResult.next, bracketWrappedFinders)
          if (hrefFinderResult.success) {
            return SubstringFinderResult.ofSuccess(text, hrefFinderResult.range, hrefFinderResult.next)
          }
        }

        return findNest(text, startAt until text.length, 0, recursiveDepthLimit)
      }

      private fun findNest(text: String, findRange: IntRange, depth: Int, limit: Int): SubstringFinderResult {
        if (depth > limit) {
          return SubstringFinderResult.ofFailure(text, IntRange.EMPTY, -1)
        }

        val bracketResult = SubstringFinderUtils.sequential(text, findRange.first, bracketWrappedFinders)
        if (bracketResult.success) {
          val wordResult = bracketResult.nests[1]
          return findNest(text, wordResult.range, depth + 1, limit)
        }

        val squareBracketResult = SubstringFinderUtils.sequential(text, findRange.first, squareBracketWrappedFinders)
        if (squareBracketResult.success) {
          val wordResult = squareBracketResult.nests[1]
          return findNest(text, wordResult.range, depth + 1, limit)
        }

        return wordFinder.find(text, findRange.first)
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
    val url = input.substring(proceedResult.range)
    return ParserResult.ofSuccess(MfmUrl(url, false), input, result.range, result.next)
  }

  private fun processTrailingPeriodAndComma(
    input: String,
    finderResult: SubstringFinderResult
  ): SubstringFinderResult {
    val extractUrl = input.substring(finderResult.range)
    val matched = commaAndPeriodRegex.find(extractUrl)
      ?: // 末尾にピリオドやカンマがない場合はそのまま返す
      return finderResult

    // finderResultの段階でuntilされているので、ここではやらない（多重にやると範囲がおかしくなる）
    val modifyRange = finderResult.range.first..finderResult.range.last - matched.value.length
    if (modifyRange.isEmpty()) {
      // スキーマ形式で始まるがカンマとピリオドのみの場合はスキーマ形式として認識しない
      return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, finderResult.next)
    }

    return SubstringFinderResult.ofSuccess(input, modifyRange, finderResult.next)
  }

  data class Context(
    var ignoreLinkLabel: Boolean
  )
}