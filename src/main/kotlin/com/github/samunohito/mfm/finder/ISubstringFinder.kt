package com.github.samunohito.mfm.finder

interface ISubstringFinder {
  fun find(input: String, startAt: Int = 0): ISubstringFinderResult

  fun optional(): ISubstringFinder {
    return Optional(this)
  }

  fun not(): ISubstringFinder {
    return Not(this)
  }

  private class Optional(private val delegate: ISubstringFinder) : ISubstringFinder {
    override fun find(input: String, startAt: Int): ISubstringFinderResult {
      val result = delegate.find(input, startAt)
      return if (result.success) {
        result
      } else {
        CoreFinderResult.ofSuccess(IntRange.EMPTY, startAt)
      }
    }
  }

  private class Not(private val delegate: ISubstringFinder) : ISubstringFinder {
    override fun find(input: String, startAt: Int): ISubstringFinderResult {
      val result = delegate.find(input, startAt)
      return if (result.success) {
        CoreFinderResult.ofFailure()
      } else {
        CoreFinderResult.ofSuccess(startAt..startAt, startAt, result.subResults)
      }
    }
  }
}