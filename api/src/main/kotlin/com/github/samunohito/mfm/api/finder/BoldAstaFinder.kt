package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder

class BoldAstaFinder : ISubstringFinder {
  companion object {
    private val mark = StringFinder("**")
    private val boldAstaFinder = SequentialFinder(
      mark,
      InlineFinder(mark),
      mark,
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = boldAstaFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(
      FoundType.BoldAsta,
      contents.range,
      result.foundInfo.next,
      contents.sub
    )
  }
}