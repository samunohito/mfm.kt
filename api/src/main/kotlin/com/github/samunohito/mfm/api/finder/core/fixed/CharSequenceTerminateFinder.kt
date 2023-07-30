package com.github.samunohito.mfm.api.finder.core.fixed

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType

/**
 * An implementation of [ISubstringFinder] that detects whether the search start position has reached the end of the search string.
 */
object CharSequenceTerminateFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    return if (input.length <= startAt) {
      success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}