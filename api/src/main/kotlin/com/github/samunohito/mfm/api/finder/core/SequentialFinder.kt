package com.github.samunohito.mfm.api.finder.core

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.utils.SubstringFinderUtils

/**
 * An implementation of [ISubstringFinder] that sequentially searches for substrings using multiple other substring finders.
 * We expect the input string to satisfy all [ISubstringFinder] passed to [finders], and if any one of the [finders] fails, this implementation will also treat the search as failed.
 *
 * @param finders A collection of [ISubstringFinder] to be used for searching.
 */
class SequentialFinder(private val finders: Collection<ISubstringFinder>) : ISubstringFinder {
  constructor(vararg finders: ISubstringFinder) : this(finders.toList())

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    return SubstringFinderUtils.sequential(input, startAt, finders, context)
  }
}