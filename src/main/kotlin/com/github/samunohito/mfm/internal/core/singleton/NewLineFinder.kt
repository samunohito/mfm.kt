package com.github.samunohito.mfm.internal.core.singleton

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils

object NewLineFinder : ISubstringFinder {
  private val parsers = listOf(CrFinder, LfFinder, CrLfFinder)

  override fun find(input: String, startAt: Int): SubstringFinderResult {
    return SubstringFinderUtils.alternative(input, startAt, parsers)
  }
}