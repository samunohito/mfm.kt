package com.github.samunohito.mfm.finder

class ItalicTagFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return failure()
  }
}