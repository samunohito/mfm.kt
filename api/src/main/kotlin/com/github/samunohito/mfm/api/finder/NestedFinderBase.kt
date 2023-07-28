package com.github.samunohito.mfm.api.finder

abstract class NestedFinderBase(private val nestedContext: INestedContext) : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    if (nestedContext.nestLevel >= nestedContext.maximumNestLevel) {
      return failure()
    }

    nestedContext.nestLevel++
    val result = doFind(input, startAt)
    nestedContext.nestLevel--

    return result
  }

  protected abstract fun doFind(input: String, startAt: Int): ISubstringFinderResult
}