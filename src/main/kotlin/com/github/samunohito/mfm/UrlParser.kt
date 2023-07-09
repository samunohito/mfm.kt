package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.node.MfmUrl

class UrlParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  companion object {
    private val defaultContext: Context = Context(
      ignoreLinkLabel = false
    )
    private val commaAndPeriodRegex = Regex("[.,]+$")

    private val schemaFinder = RegexFinder(Regex("https?://"))
    private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-()]+"))
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

    private val labelFinders = listOf(
      openSquareBracket,
      wordFinder,
      closeSquareBracket,
    )

    private val hrefFinders = listOf(
      openBracket,
      schemaFinder,
      wordFinder,
      closeBracket,
    )

    private val urlFinders = listOf(
      openBracket.optional(),
      schemaFinder,
      wordFinder,
      closeBracket.optional(),
    )

    private class UrlFinder(val context: Context) : ISubstringFinder {
      override fun find(input: String, startAt: Int): SubstringFinderResult {
        val text = input.slice(startAt until input.length)
        for (i in text.indices) {
          val result = doFind(text, i)
          if (result.success) {
            return result
          }
        }

        return SubstringFinderResult.ofFailure()
      }

      private fun doFind(text: String, startAt: Int): SubstringFinderResult {
        val linkLabelFinderResult = SubstringFinderUtils.sequential(text, startAt, linkLabelFinders)
        if (linkLabelFinderResult.success && context.ignoreLinkLabel) {
          // リンク形式として完成している場合はリンクラベル部分を無視してhref部分をチェックしたい
          val labelFinderResult = SubstringFinderUtils.sequential(text, startAt, labelFinders)
          val hrefFinderResult = SubstringFinderUtils.sequential(text, labelFinderResult.next, hrefFinders)
          if (hrefFinderResult.success) {
            return SubstringFinderResult.ofSuccess(text, hrefFinderResult.range, hrefFinderResult.next)
          }
        } else {
          val urlFinderResult = SubstringFinderUtils.sequential(text, startAt, urlFinders)
          if (urlFinderResult.success) {
            val schema = urlFinderResult.nests[1]
            val body = urlFinderResult.nests[2]

            val urlRange = schema.range.first..body.range.last
            return SubstringFinderResult.ofSuccess(text, urlRange, urlFinderResult.next)
          }
        }

        return SubstringFinderResult.ofFailure()
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
      return SubstringFinderResult.ofFailure()
    }

    return SubstringFinderResult.ofSuccess(input, modifyRange, finderResult.next)
  }

  data class Context(
    var ignoreLinkLabel: Boolean
  )
}