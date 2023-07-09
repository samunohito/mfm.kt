package com.github.samunohito.mfm.internal.core

object SubstringFinderUtils {
  fun sequential(input: String, startAt: Int, parsers: List<ISubstringFinder>): SubstringFinderResult {
    var latestIndex = startAt
    val results = mutableListOf<SubstringFinderResult>()

    for (parser in parsers) {
      val result = parser.find(input, latestIndex)
      if (!result.success) {
        val resultRange = startAt until latestIndex
        return SubstringFinderResult.ofFailure(input, resultRange, resultRange.last + 1)
      }

      latestIndex = result.next
      results.add(result)
    }

    val resultRange = startAt until latestIndex
    return SubstringFinderResult.ofSuccess(input, resultRange, resultRange.last + 1, results)
  }

  fun alternative(input: String, startAt: Int, parsers: List<ISubstringFinder>): SubstringFinderResult {
    for (parser in parsers) {
      val result = parser.find(input, startAt)
      if (result.success) {
        return result
      }
    }

    return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, startAt)
  }

}