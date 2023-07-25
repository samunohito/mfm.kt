package com.github.samunohito.mfm.api.finder.core

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.utils.SubstringFinderUtils

class AlternateFinder(private val finders: Collection<ISubstringFinder>) : ISubstringFinder {
  constructor(vararg finders: ISubstringFinder) : this(finders.toList())

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return SubstringFinderUtils.alternate(input, startAt, finders)
  }
}