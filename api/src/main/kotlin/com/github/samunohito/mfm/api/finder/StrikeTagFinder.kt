package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder

object StrikeTagFinder : ISubstringFinder {
  private val open = StringFinder("<s>")
  private val close = StringFinder("</s>")
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
      FoundType.StrikeTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}