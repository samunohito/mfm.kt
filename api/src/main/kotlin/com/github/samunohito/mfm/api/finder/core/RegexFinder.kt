package com.github.samunohito.mfm.api.finder.core

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.failure
import com.github.samunohito.mfm.api.finder.success

class RegexFinder(regex: Regex) : ISubstringFinder {
  private val _regex = Regex("^${regex.pattern}", regex.options)

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val text = input.substring(startAt)
    val result = _regex.find(text)
    return if (result == null) {
      failure()
    } else {
      val resultRange = startAt until (startAt + result.value.length)
      success(FoundType.Core, resultRange, resultRange.last + 1)
    }
  }
}