package com.github.samunohito.mfm.api.finder.core.utils

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.utils.next

object SubstringFinderUtils {
  fun sequential(input: String, startAt: Int, finders: Collection<ISubstringFinder>): ISubstringFinderResult {
    var latestIndex = startAt
    val results = mutableListOf<SubstringFoundInfo>()

    for (finder in finders) {
      val result = finder.find(input, latestIndex)
      if (!result.success) {
        return failure()
      }

      latestIndex = result.foundInfo.resumeIndex
      results.add(result.foundInfo)
    }

    val resultRange = startAt until latestIndex
    return success(
      FoundType.Core,
      resultRange,
      resultRange,
      resultRange.next(),
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

    return failure()
  }
}