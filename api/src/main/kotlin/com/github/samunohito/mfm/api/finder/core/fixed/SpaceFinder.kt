package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.RegexFinder

/**
 * An implementation of [ISubstringFinder] that searches for whitespace characters (space, tab, and full-width space) in a given string.
 */
object SpaceFinder : ISubstringFinder {
  private val finder = RegexFinder(Regex("[\u0020\u3000\t]"))

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    return finder.find(input, startAt, context)
  }
}