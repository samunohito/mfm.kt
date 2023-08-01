package com.github.samunohito.mfm.api.parser.core.fixed

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType

/**
 * An implementation of [IMfmParser] that detects whether the search start position has reached the end of the search string.
 */
object CharSequenceTerminateParser : IMfmParser {
  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    return if (input.length <= startAt) {
      success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}