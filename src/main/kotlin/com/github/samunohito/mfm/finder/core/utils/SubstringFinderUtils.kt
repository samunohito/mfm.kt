package com.github.samunohito.mfm.finder.core.utils

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.SubstringFinderResult
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.utils.IntRangeUtils

object SubstringFinderUtils {
  fun sequential(input: String, startAt: Int, finders: Collection<ISubstringFinder>): ISubstringFinderResult {
    var latestIndex = startAt
    val results = mutableListOf<ISubstringFinderResult>()

    for (finder in finders) {
      val result = finder.find(input, latestIndex)
      if (!result.success) {
        return SubstringFinderResult.ofFailure()
      }

      latestIndex = result.foundInfo.next
      results.add(result)
    }

    val resultRange = startAt until latestIndex
    return SubstringFinderResult.ofSuccess(
      FoundType.Core,
      resultRange,
      IntRangeUtils.calcNext(resultRange),
      results
    )
  }

  fun alternate(input: String, startAt: Int, finders: Collection<ISubstringFinder>): ISubstringFinderResult {
    for (finder in finders) {
      val result = finder.find(input, startAt)
      if (result.success) {
        return result
      }
    }

    return SubstringFinderResult.ofFailure()
  }
}