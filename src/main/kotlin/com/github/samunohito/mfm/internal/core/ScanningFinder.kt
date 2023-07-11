package com.github.samunohito.mfm.internal.core

class ScanningFinder(private val scanPeriod: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val foundIndex = input.indexOf(scanPeriod, startAt)
    return if (foundIndex <= -1) {
      SubstringFinderResult.ofFailure()
    } else {
      val range = startAt until foundIndex
      SubstringFinderResult.ofSuccess(range, range.last + 1)
    }
  }
}