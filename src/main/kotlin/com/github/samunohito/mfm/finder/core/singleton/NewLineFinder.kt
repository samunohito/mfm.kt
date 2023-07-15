package com.github.samunohito.mfm.finder.core.singleton

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.utils.SubstringFinderUtils

object NewLineFinder : ISubstringFinder {
  private val parsers = listOf(CrFinder, LfFinder, CrLfFinder)

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return SubstringFinderUtils.alternate(input, startAt, parsers)
  }
}