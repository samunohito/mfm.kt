package com.github.samunohito.mfm.internal.core

abstract class CharSequenceFinderBase : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val inputRange = startAt until input.length
    for (i in inputRange) {
      val result = doFind(input, i)
      if (result.success) {
        return result
      }
    }

    return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, inputRange.last + 1)
  }

  protected abstract fun doFind(text: String, startAt: Int): SubstringFinderResult
}