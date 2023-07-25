package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.utils.SubstringFinderUtils

object NewLineFinder : ISubstringFinder {
  private val finders = listOf(CrFinder, LfFinder, CrLfFinder)

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return SubstringFinderUtils.alternate(input, startAt, finders)
  }
}