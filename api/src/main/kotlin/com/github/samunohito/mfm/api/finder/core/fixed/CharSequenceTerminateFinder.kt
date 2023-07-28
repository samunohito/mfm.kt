package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.failure
import com.github.samunohito.mfm.api.finder.success

object CharSequenceTerminateFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return if (input.length <= startAt) {
      success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}