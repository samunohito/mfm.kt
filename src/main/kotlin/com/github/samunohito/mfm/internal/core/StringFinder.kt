package com.github.samunohito.mfm.internal.core

class StringFinder(private val word: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val resultRange = startAt until (startAt + word.length)
    return if ((input.length - startAt) < word.length) {
      SubstringFinderResult.ofFailure()
    } else if (input.substring(resultRange) != word) {
      SubstringFinderResult.ofFailure()
    } else {
      SubstringFinderResult.ofSuccess(input, resultRange, resultRange.last + 1)
    }
  }
}