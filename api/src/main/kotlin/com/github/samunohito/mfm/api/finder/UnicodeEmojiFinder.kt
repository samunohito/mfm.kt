package com.github.samunohito.mfm.api.finder

object UnicodeEmojiFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return failure()
  }
}