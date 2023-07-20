package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.AlternateFinder
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder

class StrikeWaveFinder : ISubstringFinder {
  companion object {
    private val mark = StringFinder("~~")
    private val finder = SequentialFinder(
      mark,
      InlineFinder(AlternateFinder(mark, NewLineFinder)),
      mark
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = finder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.sub[1]
    return success(FoundType.StrikeWave, contents.range, result.foundInfo.next, contents.sub)
  }
}