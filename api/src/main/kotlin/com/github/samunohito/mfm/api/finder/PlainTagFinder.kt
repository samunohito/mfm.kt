package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

object PlainTagFinder : ISubstringFinder {
  private val open = StringFinder("<plain>")
  private val close = StringFinder("</plain>")
  private val plainTagFinder = SequentialFinder(
    open,
    NewLineFinder.optional(),
    SequentialScanFinder.ofUntil(NewLineFinder.optional(), close),
    NewLineFinder.optional(),
    close,
  )

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = plainTagFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[2]
    return success(
      FoundType.PlainTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}