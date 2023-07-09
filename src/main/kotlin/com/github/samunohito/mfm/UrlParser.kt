package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.node.MfmUrl

class UrlParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  companion object {
    private val defaultContext: Context = Context(
      ignoreLinkLabel = false,
      recursiveDepthLimit = 20
    )
    private val commaAndPeriodRegex = Regex("[.,]+$")

    private class UrlFinder(private val context: Context) : ISubstringFinder {
      companion object {
        private val schemaFinder = RegexFinder(Regex("https?://"))
        private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))
        private val openRegexBracket = RegexFinder(Regex("([(\\[])"))
        private val closeRegexBracket = RegexFinder(Regex("([)\\]])"))

        private val regexBracketWrappedFinders = listOf(
          openRegexBracket,
          wordFinder,
          closeRegexBracket,
        )
      }

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
        if (context.ignoreLinkLabel) {
          val scanLinkResult = UrlFinderUtils.scanLink(text, startAt)
          if (scanLinkResult.success) {
            // URLの開始部分まで一気に飛ばす
            latestIndex = scanLinkResult.hrefContents.range.first
          }
        }

        val schemaResult = schemaFinder.find(text, latestIndex)
        if (!schemaResult.success) {
          return SubstringFinderResult.ofFailure(text, IntRange.EMPTY, -1)
        }

        val bodyResults = mutableListOf<SubstringFinderResult>()
        latestIndex = schemaResult.next

        for (depth in 0 until context.recursiveDepthLimit) {
          // URLについたカッコとその中身の取得を試みる
          val bracketResult = SubstringFinderUtils.sequential(text, latestIndex, regexBracketWrappedFinders)
          if (bracketResult.success) {
            bodyResults.add(bracketResult)
            latestIndex = bracketResult.next
            continue
          }

          val wordResult = wordFinder.find(text, latestIndex)
          if (wordResult.success) {
            bodyResults.add(wordResult)
            latestIndex = wordResult.next
            continue
          }

          break
        }

        if (bodyResults.isEmpty()) {
          return SubstringFinderResult.ofFailure(text, IntRange.EMPTY, -1)
        }

        val bodyRange = bodyResults.first().range.first..bodyResults.last().range.last
        val urlRange = schemaResult.range.first..bodyRange.last
        return SubstringFinderResult.ofSuccess(
          text,
          urlRange,
          urlRange.last + 1,
          listOf(
            schemaResult,
            SubstringFinderResult.ofSuccess(
              text,
              bodyRange,
              bodyRange.last + 1,
              bodyResults
            )
          )
        )
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
    var ignoreLinkLabel: Boolean,
    val recursiveDepthLimit: Int,
  )
}