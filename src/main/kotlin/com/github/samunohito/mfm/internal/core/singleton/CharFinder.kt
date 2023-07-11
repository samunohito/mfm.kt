package com.github.samunohito.mfm.internal.core.singleton

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult

object CharFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    if (input.isEmpty()) {
      return SubstringFinderResult.ofFailure()
    }

    return SubstringFinderResult.ofSuccess(startAt until (startAt + 1), startAt + 1)
  }
}