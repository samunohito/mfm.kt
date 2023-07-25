package com.github.samunohito.mfm.api.finder

class UnicodeEmojiFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return failure()
  }
}