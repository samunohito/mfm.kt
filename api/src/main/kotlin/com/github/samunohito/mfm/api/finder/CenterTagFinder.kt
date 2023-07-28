package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

class CenterTagFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder("<center>")
    private val close = StringFinder("</center>")
  }

  private val finder = SequentialFinder(
    NewLineFinder.optional(),
    LineBeginFinder,
    open,
    NewLineFinder.optional(),
    InlineFinder(SequentialFinder(NewLineFinder.optional(), close)),
    NewLineFinder.optional(),
    close,
    LineEndFinder,
    NewLineFinder.optional(),
  )

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = finder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[4]
    return success(
      FoundType.CenterTag,
      result.foundInfo.fullRange,
      contents.contentRange,
      result.foundInfo.next,
      contents.sub
    )
  }
}