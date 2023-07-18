package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder

class PlainTagFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder("<plain>")
    private val close = StringFinder("</plain>")
    private val plainTagFinder = SequentialFinder(
      open,
      NewLineFinder.optional(),
      SequentialScanFinder.ofUntil(NewLineFinder.optional(), close),
      NewLineFinder.optional(),
      close,
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = plainTagFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[2]
    return success(FoundType.PlainTag, contents.range, result.foundInfo.next)
  }
}