package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder

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

    val contentsResult = result.foundInfo.sub[1]
    return success(FoundType.Bold, contentsResult.range, result.foundInfo.next)
  }
}