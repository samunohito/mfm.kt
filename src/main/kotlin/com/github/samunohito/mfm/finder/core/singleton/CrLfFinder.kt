package com.github.samunohito.mfm.finder.core.singleton

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.RegexFinder

object CrLfFinder : ISubstringFinder {
  private val finder = RegexFinder(Regex("\r\n"))

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return finder.find(input, startAt)
  }
}