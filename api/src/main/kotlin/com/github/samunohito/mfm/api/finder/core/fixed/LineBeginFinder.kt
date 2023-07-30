package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType

/**
 * An implementation of [ISubstringFinder] that finds the beginning of a line within a given string.
 * "beginning of line" indicates the following.
 * - If the search start position is 0
 * - If one character before the search start position is a linefeed code and the search start position is immediately after the linefeed code
 */
object LineBeginFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = when {
      input.length == startAt -> true
      startAt == 0 -> true
      CrFinder.find(input, startAt - 1, context).success -> true
      LfFinder.find(input, startAt - 1, context).success -> true
      else -> false
    }

    return if (result) {
      success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}