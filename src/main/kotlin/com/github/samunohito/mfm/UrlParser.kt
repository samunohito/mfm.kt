package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.node.MfmUrl

class UrlParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  private val urlFinder = SequentialFinder(
    RegexFinder(Regex("https?://")),
    UrlBodyFinder(context)
  )

  companion object {
    private val defaultContext: Context = Context.init()
    private val commaAndPeriodRegex = Regex("[.,]+$")

    private class UrlBodyFinder(private val context: Context) : ISubstringFinder {
      companion object {
        private val openRegexBracket = RegexFinder(Regex("([(\\[])"))
        private val closeRegexBracket = RegexFinder(Regex("([)\\]])"))
        private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))
      }

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        if (context.currentDepth > context.recursiveDepthLimit) {
          return SubstringFinderResult.ofFailure()
        }

        var latestIndex = startAt
        val bodyResults = mutableListOf<SubstringFinderResult>()

        while (true) {
          // wordFinderのパターンにカッコを含めると、終了カッコのみを誤検出してしまう。
          // …ので、サポートされている種類の開始・終了カッコが揃っている場合は、その中身を再帰的に検索する
          val regexBracketWrappedFinders = listOf(
            openRegexBracket,
            UrlBodyFinder(context.incrementDepth()),
            closeRegexBracket,
          )
          val bracketResult = SubstringFinderUtils.sequential(input, latestIndex, regexBracketWrappedFinders)
          if (bracketResult.success) {
            bodyResults.add(bracketResult)
            latestIndex = bracketResult.next
            continue
          }

          val wordResult = wordFinder.find(input, latestIndex)
          if (wordResult.success) {
            bodyResults.add(wordResult)
            latestIndex = wordResult.next
            continue
          }

          break
        }

        if (bodyResults.isEmpty()) {
          return SubstringFinderResult.ofFailure()
        }

        val bodyRange = bodyResults.first().range.first..bodyResults.last().range.last
        return SubstringFinderResult.ofSuccess(bodyRange, bodyRange.last + 1, bodyResults)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmUrl> {
    if (context.disabled) {
      return ParserResult.ofFailure()
    }

    val result = urlFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val proceedResult = processTrailingPeriodAndComma(input, result)
    if (!proceedResult.success) {
      return ParserResult.ofFailure()
    }

    val url = input.substring(proceedResult.range)
    return ParserResult.ofSuccess(MfmUrl(url, false), result.range, result.next)
  }

  /**
   * URLの末尾にピリオドやカンマがある場合は、それを除外した範囲を作成する
   */
  private fun processTrailingPeriodAndComma(
    input: String,
    finderResult: SubstringFinderResult
  ): SubstringFinderResult {
    val body = finderResult.subResults[1]
    val extractUrlBody = input.substring(body.range)
    val matched = commaAndPeriodRegex.find(extractUrlBody)
      ?: // 末尾にピリオドやカンマがない場合はそのまま返す
      return finderResult

    if (extractUrlBody.length - matched.value.length <= 0) {
      // スキーマ形式で始まるが、それ以降がカンマとピリオドのみの場合はスキーマ形式として認識しない
      return SubstringFinderResult.ofFailure()
    }

    // finderResultの段階でuntilされているので、ここではやらない（多重にやると範囲がおかしくなる）
    val modifyRange = finderResult.range.first..finderResult.range.last - matched.value.length
    return SubstringFinderResult.ofSuccess(modifyRange, finderResult.next)
  }

  class Context private constructor(
    var disabled: Boolean,
    val currentDepth: Int,
    val recursiveDepthLimit: Int,
  ) {
    companion object {
      fun init(recursiveDepthLimit: Int = 20): Context {
        return Context(
          disabled = false,
          currentDepth = 0,
          recursiveDepthLimit = recursiveDepthLimit
        )
      }
    }

    fun incrementDepth(): Context {
      return Context(
        disabled = disabled,
        currentDepth = currentDepth + 1,
        recursiveDepthLimit = recursiveDepthLimit
      )
    }
  }
}