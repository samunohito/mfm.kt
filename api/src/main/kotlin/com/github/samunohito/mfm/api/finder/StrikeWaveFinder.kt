package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.AlternateFinder
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

class StrikeWaveFinder : ISubstringFinder {
  companion object {
    private val mark = StringFinder("~~")
  }

  private val finder = SequentialFinder(
    mark,
    InlineFinder(AlternateFinder(mark, NewLineFinder)),
    mark
  )

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = finder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(
      FoundType.StrikeWave,
      result.foundInfo.fullRange,
      contents.contentRange,
      result.foundInfo.next,
      contents.sub
    )
  }
}