package com.github.samunohito.mfm.internal.core

object SubstringFinderUtils {
  fun sequential(input: String, startAt: Int, finders: Collection<ISubstringFinder>): SubstringFinderResult {
    var latestIndex = startAt
    val results = mutableListOf<SubstringFinderResult>()

    for (finder in finders) {
      val result = finder.find(input, latestIndex)
      if (!result.success) {
        return SubstringFinderResult.ofFailure()
      }

      latestIndex = result.next
      results.add(result)
    }

    val resultRange = startAt until latestIndex
    return SubstringFinderResult.ofSuccess(resultRange, resultRange.last + 1, results)
  }

  fun alternate(input: String, startAt: Int, finders: Collection<ISubstringFinder>): SubstringFinderResult {
    for (finder in finders) {
      val result = finder.find(input, startAt)
      if (result.success) {
        return result
      }
    }

    return SubstringFinderResult.ofFailure()
  }

}