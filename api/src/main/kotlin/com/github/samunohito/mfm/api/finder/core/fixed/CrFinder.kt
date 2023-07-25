package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.StringFinder

object CrFinder : ISubstringFinder {
  private val finder = StringFinder("\r")

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    return finder.find(input, startAt)
  }
}