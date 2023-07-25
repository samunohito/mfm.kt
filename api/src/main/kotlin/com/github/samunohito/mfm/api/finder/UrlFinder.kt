package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.AlternateFinder
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

class UrlFinder : ISubstringFinder {
  companion object {
    private val regexCommaAndPeriodTail = Regex("[.,]+$")
    private val urlFinder = SequentialFinder(
      RegexFinder(Regex("https?://")),
      UrlBodyFinder
    )

    private object UrlBodyFinder : ISubstringFinder {
      private val openRegexBracket = RegexFinder(Regex("([(\\[])"))
      private val closeRegexBracket = RegexFinder(Regex("([)\\]])"))
      private val nestableFinder = AlternateFinder(
        // パターンにカッコを含めると、終了カッコのみを誤検出してしまう。
        // …ので、サポートされている種類の開始・終了カッコが揃っている場合は、その中身を再帰的に検索する
        SequentialFinder(
          openRegexBracket,
          UrlBodyFinder,
          closeRegexBracket,
        ),
        // このパターンに合致する文字が登場するまでを繰り返し検索する
        RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+")),
      )

      override fun find(input: String, startAt: Int): ISubstringFinderResult {
        var latestIndex = startAt
        val foundInfos = mutableListOf<SubstringFoundInfo>()

        while (true) {
          val result = nestableFinder.find(input, latestIndex)
          if (!result.success) {
            break
          }

          foundInfos.add(result.foundInfo)
          latestIndex = result.foundInfo.next
        }

        if (foundInfos.isEmpty()) {
          return failure()
        }

        val resultRange = foundInfos.map { it.range }.merge()
        return success(FoundType.Url, resultRange, resultRange.next(), foundInfos)
      }
    }
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = urlFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val proceedResult = processTrailingPeriodAndComma(input, result)
    if (!proceedResult.success) {
      return failure()
    }

    return success(FoundType.Url, proceedResult)
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
      return failure()
    }

    // finderResultの段階でuntilされているので、ここではやらない（多重にやると範囲がおかしくなる）
    val modifyRange = foundInfo.range.first..foundInfo.range.last - matched.value.length
    return success(FoundType.Url, modifyRange, modifyRange.next())
  }
}