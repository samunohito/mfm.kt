package com.github.samunohito.mfm.api.finder.core.charsequence

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType

/**
 * Returns the range from the search start position to where [scanPeriod] appears as the search result.
 */
class ScanningFinder(private val scanPeriod: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val foundIndex = input.indexOf(scanPeriod, startAt)
    return if (foundIndex <= -1) {
      failure()
    } else {
      val range = startAt until foundIndex
      success(FoundType.Core, range, range, range.last + 1)
    }
  }
}