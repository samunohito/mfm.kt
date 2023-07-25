package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.StringFinder

object CrLfFinder : ISubstringFinder {
  private val finder = StringFinder("\r\n")

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return finder.find(input, startAt)
  }
}