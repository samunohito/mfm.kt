package com.github.samunohito.mfm.internal.core.singleton

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult

object LineBeginFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val result = when {
      input.length == startAt -> true
      startAt == 0 -> true
      CrFinder.find(input, startAt - 1).success -> true
      LfFinder.find(input, startAt - 1).success -> true
      else -> false
    }

    return if (result) {
      SubstringFinderResult.ofSuccess(input, startAt..startAt, startAt)
    } else {
      SubstringFinderResult.ofFailure()
    }
  }
}