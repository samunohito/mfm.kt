package com.github.samunohito.mfm.finder

class SmallTagFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return failure()
  }
}