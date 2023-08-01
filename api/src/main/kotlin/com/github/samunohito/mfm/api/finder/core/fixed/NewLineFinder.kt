package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.AlternateFinder

/**
 * An implementation of [ISubstringFinder] that searches for newline characters (line feed, carriage return, and carriage return followed by line feed) in a given string
 */
object NewLineFinder : ISubstringFinder {
  private val finder = AlternateFinder(CrFinder, LfFinder, CrLfFinder)

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    return finder.find(input, startAt, context)
  }
}