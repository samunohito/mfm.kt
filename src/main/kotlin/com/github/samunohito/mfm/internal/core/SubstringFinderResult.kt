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

    fun ofFailure(
      range: IntRange = IntRange.EMPTY,
      next: Int = -1,
      nestResult: List<SubstringFinderResult> = listOf()
    ): SubstringFinderResult {
      return SubstringFinderResult(false, range, next, nestResult)
    }
  }
}
