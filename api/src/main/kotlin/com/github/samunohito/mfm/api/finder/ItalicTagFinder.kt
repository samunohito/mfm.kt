package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder

object ItalicTagFinder : ISubstringFinder {
  private val open = StringFinder("<i>")
  private val close = StringFinder("</i>")
  private val finder = SequentialFinder(
    open,
    InlineFinder(close),
    close
  )

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = finder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.ItalicTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}