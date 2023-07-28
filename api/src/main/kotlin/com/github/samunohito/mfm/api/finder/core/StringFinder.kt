package com.github.samunohito.mfm.api.finder.core

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.failure
import com.github.samunohito.mfm.api.finder.success
import com.github.samunohito.mfm.api.utils.next

class StringFinder(private val word: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val resultRange = startAt until (startAt + word.length)
    return if ((input.length - startAt) < word.length) {
      failure()
    } else if (input.substring(resultRange) != word) {
      failure()
    } else {
      success(FoundType.Core, resultRange, resultRange, resultRange.next())
    }
  }
}