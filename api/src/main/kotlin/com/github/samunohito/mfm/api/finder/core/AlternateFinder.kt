package com.github.samunohito.mfm.api.finder.core

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.utils.SubstringFinderUtils

/**
 * An implementation of [ISubstringFinder] that searches for a given string in a given string.
 * The input string is expected to satisfy any [ISubstringFinder] passed to [finders]. If all [finders] fail, this implementation treats the search as failing as well.
 *
 * @param finders A collection of [ISubstringFinder] to be used for searching.
 */
class AlternateFinder(private val finders: Collection<ISubstringFinder>) : ISubstringFinder {
  constructor(vararg finders: ISubstringFinder) : this(finders.toList())

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    return SubstringFinderUtils.alternate(input, startAt, finders, context)
  }
}