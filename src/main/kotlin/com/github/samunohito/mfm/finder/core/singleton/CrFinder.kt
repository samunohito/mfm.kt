package com.github.samunohito.mfm.finder.core.singleton

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.core.RegexFinder

object CrFinder : ISubstringFinder {
  private val finder = RegexFinder(Regex("\r"))

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return finder.find(input, startAt)
  }
}