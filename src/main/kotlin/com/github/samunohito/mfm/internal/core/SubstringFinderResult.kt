package com.github.samunohito.mfm.internal.core

class SubstringFinderResult private constructor(
  val success: Boolean,
  val input: String,
  val range: IntRange,
  val next: Int,
  val nests: List<SubstringFinderResult> = emptyList()
) {

  companion object {
    fun ofSuccess(
      input: String,
      range: IntRange,
      next: Int,
      nestResult: List<SubstringFinderResult> = emptyList()
    ): SubstringFinderResult {
      return SubstringFinderResult(true, input, range, next, nestResult)
    }

    fun ofFailure(): SubstringFinderResult {
      return SubstringFinderResult(false, "", IntRange.EMPTY, -1)
    }
  }
}
