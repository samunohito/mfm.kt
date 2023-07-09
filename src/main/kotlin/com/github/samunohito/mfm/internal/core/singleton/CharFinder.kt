package com.github.samunohito.mfm.internal.core.singleton

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult

object CharFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    if (input.isEmpty()) {
      return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, -1)
    }

    return SubstringFinderResult.ofSuccess(input, startAt until (startAt + 1), startAt + 1)
  }
}