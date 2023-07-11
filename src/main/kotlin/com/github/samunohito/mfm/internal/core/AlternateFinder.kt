package com.github.samunohito.mfm.internal.core

class AlternateFinder(private val finders : Collection<ISubstringFinder>) : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    return SubstringFinderUtils.alternate(input, startAt, finders)
  }
}