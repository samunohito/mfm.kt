package com.github.samunohito.mfm.finder.core.singleton

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.failure
import com.github.samunohito.mfm.finder.success

object LineEndFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = when {
      input.length == startAt -> true
      CrFinder.find(input, startAt).success -> true
      LfFinder.find(input, startAt).success -> true
      else -> false
    }

    return if (result) {
      success(FoundType.Core, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}