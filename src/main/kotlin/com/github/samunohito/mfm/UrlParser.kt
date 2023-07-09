package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils
import com.github.samunohito.mfm.node.MfmUrl

class UrlParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  companion object {
    private val defaultContext: Context = Context(
      disabled = false,
      recursiveDepthLimit = 20
    )
    private val commaAndPeriodRegex = Regex("[.,]+$")
    private val schemaFinder = RegexFinder(Regex("https?://"))
    private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))

    private class UrlFinder(private val context: Context) : ISubstringFinder {
      companion object {
        private val openRegexBracket = RegexFinder(Regex("([(\\[])"))
        private val closeRegexBracket = RegexFinder(Regex("([)\\]])"))

        private val regexBracketWrappedFinders = listOf(
          openRegexBracket,
          wordFinder,
          closeRegexBracket,
        )
      }

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        var latestIndex = startAt
        val bodyResults = mutableListOf<SubstringFinderResult>()

        for (depth in 0 until context.recursiveDepthLimit) {
          val wordResult = wordFinder.find(input, latestIndex)
          if (wordResult.success) {
            bodyResults.add(wordResult)
            latestIndex = wordResult.next
            continue
          }

          // URLについたカッコとその中身の取得を試みる
          // wordFinderのパターンにカッコを含めるとリンク形式の終了カッコ検出時に支障が出るので、
          // URL中のみで開始～終了のカッコが揃っているパターンを検出したい
          val bracketResult = SubstringFinderUtils.sequential(input, latestIndex, regexBracketWrappedFinders)
          if (bracketResult.success) {
            bodyResults.add(bracketResult)
            latestIndex = bracketResult.next
            continue
          }

          break
        }

        if (bodyResults.isEmpty()) {
          return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, -1)
        }

        val bodyRange = bodyResults.first().range.first..bodyResults.last().range.last
        return SubstringFinderResult.ofSuccess(input, bodyRange, bodyRange.last + 1, bodyResults)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmUrl> {
    if (context.disabled) {
      return ParserResult.ofFailure()
    }

    val result = SubstringFinderUtils.sequential(input, startAt, listOf(schemaFinder, UrlFinder(context)))
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
    var disabled: Boolean,
    val recursiveDepthLimit: Int,
  )
}