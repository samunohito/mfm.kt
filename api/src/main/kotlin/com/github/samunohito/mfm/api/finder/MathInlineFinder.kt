package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

object MathInlineFinder : ISubstringFinder {
  private val open = StringFinder(word = "\\(")
  private val close = StringFinder("\\)")
  private val mathInlineFinder = SequentialFinder(
    open,
    AlternateScanFinder.ofUntil(close, NewLineFinder),
    close
  )

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = mathInlineFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(FoundType.MathInline, result.foundInfo.fullRange, contents.contentRange, result.foundInfo.next)
  }
}