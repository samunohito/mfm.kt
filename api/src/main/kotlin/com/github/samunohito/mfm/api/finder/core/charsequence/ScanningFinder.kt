package com.github.samunohito.mfm.api.finder.core.charsequence

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.failure
import com.github.samunohito.mfm.api.finder.success

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