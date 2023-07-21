package com.github.samunohito.mfm.finder.core.fixed

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.RegexFinder
import com.github.samunohito.mfm.finder.core.StringFinder

object CrLfFinder : ISubstringFinder {
  private val finder = StringFinder("\r\n")

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return finder.find(input, startAt)
  }
}