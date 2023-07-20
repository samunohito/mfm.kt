package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder

class BigFinder : ISubstringFinder {
  companion object {
    private val mark = StringFinder("***")
    private val finder = SequentialFinder(
      mark,
      InlineFinder(mark),
      mark
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = finder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(FoundType.Big, contents.range, result.foundInfo.next, contents.sub)
  }
}