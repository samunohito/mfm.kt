package com.github.samunohito.mfm.internal.core.singleton

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult

object CrFinder : ISubstringFinder {
  private val finder = RegexFinder(Regex("\r"))

  override fun find(input: String, startAt: Int): SubstringFinderResult {
    return finder.find(input, startAt)
  }
}