package com.github.samunohito.mfm.internal.core

abstract class CharSequenceFinderBase : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val inputRange = startAt until input.length
    val text = input.slice(inputRange)

    val foundRangeLast = text.indices.firstOrNull { !hasNext(text, it) }
      ?: return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, inputRange.last + 1)
    val foundRange = (startAt until (startAt + foundRangeLast))
    return SubstringFinderResult.ofSuccess(input, foundRange, foundRange.last + 1)
  }

  protected abstract fun hasNext(text: String, startAt: Int): Boolean
}