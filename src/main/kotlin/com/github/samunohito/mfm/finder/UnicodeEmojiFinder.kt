package com.github.samunohito.mfm.finder

class UnicodeEmojiFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return SubstringFinderResult.ofFailure()
  }
}