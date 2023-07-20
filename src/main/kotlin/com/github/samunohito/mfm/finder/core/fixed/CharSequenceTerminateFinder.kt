package com.github.samunohito.mfm.finder.core.fixed

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.failure
import com.github.samunohito.mfm.finder.success

object CharSequenceTerminateFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return if (input.length <= startAt) {
      success(FoundType.Core, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}