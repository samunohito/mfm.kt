package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.failure
import com.github.samunohito.mfm.api.finder.success

object LineEndFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = when {
      input.length == startAt -> true
      CrFinder.find(input, startAt).success -> true
      LfFinder.find(input, startAt).success -> true
      else -> false
    }

    return if (result) {
      success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}