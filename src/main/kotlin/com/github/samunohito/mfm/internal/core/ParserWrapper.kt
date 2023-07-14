package com.github.samunohito.mfm.internal.core

import com.github.samunohito.mfm.IParser

class ParserWrapper(private val parser: IParser<*>) : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val result = parser.parse(input, startAt)
    if (!result.success) {
      return SubstringFinderResult.ofFailure()
    }

    return SubstringFinderResult.ofSuccess(result.range, result.next)
  }
}