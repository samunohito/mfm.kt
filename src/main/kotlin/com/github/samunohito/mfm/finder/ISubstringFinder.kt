package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType

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
        success(FoundType.Core, IntRange.EMPTY, startAt)
      }
    }
  }

  private class Not(private val delegate: ISubstringFinder) : ISubstringFinder {
    override fun find(input: String, startAt: Int): ISubstringFinderResult {
      val result = delegate.find(input, startAt)
      return if (result.success) {
        failure()
      } else {
        success(FoundType.Core, result.foundInfo)
      }
    }
  }
}