package com.github.samunohito.mfm.finder.core

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.failure
import com.github.samunohito.mfm.finder.success

class ScanningFinder(private val scanPeriod: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val foundIndex = input.indexOf(scanPeriod, startAt)
    return if (foundIndex <= -1) {
      failure()
    } else {
      val range = startAt until foundIndex
      success(FoundType.Core, range, range.last + 1)
    }
  }
}