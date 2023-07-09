package com.github.samunohito.mfm.internal.core

class StringFinder(private val word: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val resultRange = startAt until (startAt + word.length)
    val next = resultRange.last + 1
    return if ((input.length - startAt) < word.length) {
      SubstringFinderResult.ofFailure(input, IntRange.EMPTY, next)
    } else if (input.substring(resultRange) != word) {
      SubstringFinderResult.ofFailure(input, IntRange.EMPTY, next)
    } else {
      SubstringFinderResult.ofSuccess(input, resultRange, next)
    }
  }
}