package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.RegexFinder
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.utils.SubstringFinderUtils
import com.github.samunohito.mfm.utils.merge
import com.github.samunohito.mfm.utils.next

class UrlFinder(private val context: Context = Context.init()) : ISubstringFinder {
  private val urlFinder = SequentialFinder(
    RegexFinder(Regex("https?://")),
    UrlBodyFinder(context)
  )

  companion object {
    private val regexCommaAndPeriodTail = Regex("[.,]+$")

    private class UrlBodyFinder(private val context: Context) : ISubstringFinder {
      companion object {
        private val openRegexBracket = RegexFinder(Regex("([(\\[])"))
        private val closeRegexBracket = RegexFinder(Regex("([)\\]])"))
        private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))
      }

      override fun find(input: String, startAt: Int): ISubstringFinderResult {
        if (context.currentDepth > context.recursiveDepthLimit) {
          return SubstringFinderResult.ofFailure()
        }

        var latestIndex = startAt
        val foundInfos = mutableListOf<SubstringFoundInfo>()

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
            foundInfos.add(bracketResult.foundInfo)
            latestIndex = bracketResult.foundInfo.next
            continue
          }

          val wordResult = wordFinder.find(input, latestIndex)
          if (wordResult.success) {
            foundInfos.add(wordResult.foundInfo)
            latestIndex = wordResult.foundInfo.next
            continue
          }

          break
        }

        if (foundInfos.isEmpty()) {
          return SubstringFinderResult.ofFailure()
        }

        val resultRange = foundInfos.map { it.range }.merge()
        return SubstringFinderResult.ofSuccess(FoundType.Url, resultRange, resultRange.next(), foundInfos)
      }
    }
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = urlFinder.find(input, startAt)
    if (!result.success) {
      return SubstringFinderResult.ofFailure()
    }

    val proceedResult = processTrailingPeriodAndComma(input, result)
    if (!proceedResult.success) {
      return SubstringFinderResult.ofFailure()
    }

    return SubstringFinderResult.ofSuccess(FoundType.Url, proceedResult)
  }

  /**
   * URLの末尾にピリオドやカンマがある場合は、それを除外した範囲を作成する
   */
  private fun processTrailingPeriodAndComma(
    input: String,
    finderResult: ISubstringFinderResult
  ): ISubstringFinderResult {
    val foundInfo = finderResult.foundInfo
    val body = foundInfo.sub[1]
    val extractUrlBody = input.substring(body.range)
    val matched = regexCommaAndPeriodTail.find(extractUrlBody)
      ?: // 末尾にピリオドやカンマがない場合はそのまま返す
      return finderResult

    if (extractUrlBody.length - matched.value.length <= 0) {
      // スキーマ形式で始まるが、それ以降がカンマとピリオドのみの場合はスキーマ形式として認識しない
      return SubstringFinderResult.ofFailure()
    }

    // finderResultの段階でuntilされているので、ここではやらない（多重にやると範囲がおかしくなる）
    val modifyRange = foundInfo.range.first..foundInfo.range.last - matched.value.length
    return SubstringFinderResult.ofSuccess(FoundType.Url, modifyRange, foundInfo.next)
  }

  class Context private constructor(
    val currentDepth: Int,
    val recursiveDepthLimit: Int,
  ) {
    companion object {
      fun init(recursiveDepthLimit: Int = 20): Context {
        return Context(
          currentDepth = 0,
          recursiveDepthLimit = recursiveDepthLimit
        )
      }
    }

    fun incrementDepth(): Context {
      return Context(
        currentDepth = currentDepth + 1,
        recursiveDepthLimit = recursiveDepthLimit
      )
    }
  }
}