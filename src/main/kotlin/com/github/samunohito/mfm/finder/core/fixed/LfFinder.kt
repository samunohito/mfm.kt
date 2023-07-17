package com.github.samunohito.mfm.finder.core.fixed

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.RegexFinder

object LfFinder : ISubstringFinder {
  private val finder = RegexFinder(Regex("\n"))

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return finder.find(input, startAt)
  }
}