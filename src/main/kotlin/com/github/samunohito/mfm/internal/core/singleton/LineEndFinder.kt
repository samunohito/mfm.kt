package com.github.samunohito.mfm.internal.core.singleton

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult

object LineEndFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val result = when {
      input.length == startAt -> true
      CrFinder.find(input, startAt).success -> true
      LfFinder.find(input, startAt).success -> true
      else -> false
    }

    return if (result) {
      SubstringFinderResult.ofSuccess(input, startAt..startAt, startAt)
    } else {
      SubstringFinderResult.ofFailure()
    }
  }
}