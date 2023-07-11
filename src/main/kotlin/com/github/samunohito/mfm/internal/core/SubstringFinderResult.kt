package com.github.samunohito.mfm.internal.core

class SubstringFinderResult private constructor(
  val success: Boolean,
  val range: IntRange,
  val next: Int,
  val subResults: List<SubstringFinderResult>
) {

  companion object {
    fun ofSuccess(
      range: IntRange,
      next: Int,
      nestResult: List<SubstringFinderResult> = listOf()
    ): SubstringFinderResult {
      return SubstringFinderResult(true, range, next, nestResult)
    }

    fun ofFailure(): SubstringFinderResult {
      return SubstringFinderResult(false, IntRange.EMPTY, -1, listOf())
    }
  }
}
