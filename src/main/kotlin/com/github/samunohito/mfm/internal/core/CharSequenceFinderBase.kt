package com.github.samunohito.mfm.internal.core

abstract class CharSequenceFinderBase: ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val text = input.slice(startAt until input.length)
    val rangeLast = text.indices.firstOrNull { !hasNext(text, it) }
      ?: return SubstringFinderResult.ofFailure()

    val range = (startAt until (startAt + rangeLast))
    return SubstringFinderResult.ofSuccess(input, range, range.last + 1)
  }

  protected abstract fun hasNext(text: String, startAt: Int): Boolean
}