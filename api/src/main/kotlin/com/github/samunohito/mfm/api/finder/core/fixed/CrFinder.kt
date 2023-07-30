package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.StringFinder

/**
 * An implementation of [ISubstringFinder] that searches for carriage return characters in a given string.
 */
object CrFinder : ISubstringFinder {
  private val finder = StringFinder("\r")

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    return finder.find(input, startAt, context)
  }
}