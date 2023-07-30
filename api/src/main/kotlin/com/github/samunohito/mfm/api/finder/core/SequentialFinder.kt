package com.github.samunohito.mfm.api.finder.core

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.utils.SubstringFinderUtils

class SequentialFinder(private val finders: Collection<ISubstringFinder>) : ISubstringFinder {
  constructor(vararg finders: ISubstringFinder) : this(finders.toList())

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    return SubstringFinderUtils.sequential(input, startAt, finders, context)
  }
}