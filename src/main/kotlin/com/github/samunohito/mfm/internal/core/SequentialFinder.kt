package com.github.samunohito.mfm.internal.core

class SequentialFinder(private val finders: Collection<ISubstringFinder>) : ISubstringFinder {
  constructor(vararg finders: ISubstringFinder) : this(finders.toList())

  override fun find(input: String, startAt: Int): SubstringFinderResult {
    return SubstringFinderUtils.sequential(input, startAt, finders)
  }
}