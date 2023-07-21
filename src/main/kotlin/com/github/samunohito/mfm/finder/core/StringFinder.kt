package com.github.samunohito.mfm.finder.core

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.failure
import com.github.samunohito.mfm.finder.success
import com.github.samunohito.mfm.utils.next

class StringFinder(private val word: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val resultRange = startAt until (startAt + word.length)
    return if ((input.length - startAt) < word.length) {
      failure()
    } else if (input.substring(resultRange) != word) {
      failure()
    } else {
      success(FoundType.Core, resultRange, resultRange.next())
    }
  }
}