package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.api.finder.core.fixed.SpaceFinder
import com.github.samunohito.mfm.api.utils.next

class QuoteFinder : ISubstringFinder {
  companion object {
    private object QuoteLinesFinder : ISubstringFinder {
      private val oneLineFinder = SequentialFinder(
        StringFinder(">"),
        SpaceFinder.optional(),
        SequentialScanFinder.ofUntil(NewLineFinder).optional(),
        NewLineFinder.optional(),
      )

      override fun find(input: String, startAt: Int): ISubstringFinderResult {
        var latestIndex = startAt
        val lines = mutableListOf<SubstringFoundInfo>()
        while (true) {
          val result = oneLineFinder.find(input, latestIndex)
          if (!result.success) {
            break
          }

          latestIndex = result.foundInfo.next

          // 引用符を省いた本文部分だけを蓄積したい
          lines.add(result.foundInfo[2])
        }

        if (startAt == latestIndex || lines.isEmpty()) {
          // 引用符つきの行が見当たらないときは認識しない
          return failure()
        }

        if (lines.size == 1 && lines[0].range.isEmpty()) {
          // 引用符つきの行が1行のみで、なおかつ引用符のみの行だったときは認識しない
          return failure()
        }

        val range = startAt..latestIndex
        return success(FoundType.Quote, range, range.next(), lines)
      }
    }
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = QuoteLinesFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    return success(FoundType.Quote, result.foundInfo)
  }
}