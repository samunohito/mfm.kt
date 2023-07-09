package com.github.samunohito.mfm.internal.core

interface ISubstringFinder {
  fun find(input: String, startAt: Int = 0): SubstringFinderResult

  fun optional(): ISubstringFinder {
    return Optional(this)
  }

  fun not(): ISubstringFinder {
    return Not(this)
  }

  private class Optional(private val delegate: ISubstringFinder) : ISubstringFinder {
    override fun find(input: String, startAt: Int): SubstringFinderResult {
      val result = delegate.find(input, startAt)
      return if (result.success) {
        result
      } else {
        SubstringFinderResult.ofSuccess(input, IntRange.EMPTY, startAt)
      }
    }
  }

  private class Not(private val delegate: ISubstringFinder) : ISubstringFinder {
    override fun find(input: String, startAt: Int): SubstringFinderResult {
      val result = delegate.find(input, startAt)
      return if (result.success) {
        SubstringFinderResult.ofFailure(input, result.range, result.next, result.nests)
      } else {
        SubstringFinderResult.ofSuccess(input, result.range, result.next, result.nests)
      }
    }
  }
}