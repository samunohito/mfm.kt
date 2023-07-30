package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType

/**
 * An implementation of [ISubstringFinder] that searches for the end of a line in a given string.
 * "End of line" indicates the following.
 * - When the search start position is the end of the string (when the length of the string and the search start position are the same)
 * - When the position to start searching is a line feed code
 */
object LineEndFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = when {
      input.length == startAt -> true
      CrFinder.find(input, startAt, context).success -> true
      LfFinder.find(input, startAt, context).success -> true
      else -> false
    }

    return if (result) {
      success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}