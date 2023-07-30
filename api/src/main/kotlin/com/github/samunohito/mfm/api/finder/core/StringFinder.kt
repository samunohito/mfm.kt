package com.github.samunohito.mfm.api.finder.core

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.utils.next

/**
 * An implementation of [ISubstringFinder] that searches for a given string in a given string.
 */
class StringFinder(private val word: String) : ISubstringFinder {
  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
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