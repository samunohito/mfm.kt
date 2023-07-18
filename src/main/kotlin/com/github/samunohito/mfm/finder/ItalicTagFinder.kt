package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder

class ItalicTagFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder("<i>")
    private val close = StringFinder("</i>")
    private val finder = SequentialFinder(
      open,
      InlineFinder(close),
      close
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = finder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(FoundType.ItalicTag, contents.range, result.foundInfo.next)
  }
}