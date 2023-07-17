package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder

class MathBlockFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder("\\[")
    private val close = StringFinder("\\]")
    private val mathBlockFinder = SequentialFinder(
      NewLineFinder.optional(),
      LineBeginFinder,
      open,
      NewLineFinder.optional(),
      SequentialScanFinder.ofUntil(NewLineFinder.optional(), close),
      NewLineFinder.optional(),
      close,
      LineEndFinder,
      NewLineFinder.optional()
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = mathBlockFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contentsResult = result.foundInfo.sub[4]
    return success(FoundType.MathBlock, contentsResult.range, result.foundInfo.next)
  }
}