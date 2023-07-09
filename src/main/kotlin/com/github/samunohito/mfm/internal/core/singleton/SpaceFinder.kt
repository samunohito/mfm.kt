package com.github.samunohito.mfm.internal.core.singleton

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult

object SpaceFinder : ISubstringFinder {
  private val finder = RegexFinder(Regex("[\u0020\u3000\t]"))

  override fun find(input: String, startAt: Int): SubstringFinderResult {
    return finder.find(input, startAt)
  }
}