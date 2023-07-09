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

    fun ofFailure(
      input: String,
      range: IntRange,
      next: Int,
    ): SubstringFinderResult {
      return SubstringFinderResult(false, input, range, next)
    }
  }
}
