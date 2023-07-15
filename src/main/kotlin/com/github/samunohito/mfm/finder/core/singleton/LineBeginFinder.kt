package com.github.samunohito.mfm.finder.core.singleton

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.failure
import com.github.samunohito.mfm.finder.success

object LineBeginFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = when {
      input.length == startAt -> true
      startAt == 0 -> true
      CrFinder.find(input, startAt - 1).success -> true
      LfFinder.find(input, startAt - 1).success -> true
      else -> false
    }

    return if (result) {
      success(FoundType.Core, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}