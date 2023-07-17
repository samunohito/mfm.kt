package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder

class MathInlineFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder(word = "\\(")
    private val close = StringFinder("\\)")
    private val mathInlineFinder = SequentialFinder(
      open,
      AlternateScanFinder.ofUntil(close, NewLineFinder),
      close
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = mathInlineFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    return success(FoundType.MathInline, result.foundInfo.range, result.foundInfo.next)
  }
}