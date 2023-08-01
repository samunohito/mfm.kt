package com.github.samunohito.mfm.api.parser.core.fixed

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType

/**
 * An implementation of [IMfmParser] that finds the beginning of a line within a given string.
 * "beginning of line" indicates the following.
 * - If the search start position is 0
 * - If one character before the search start position is a linefeed code and the search start position is immediately after the linefeed code
 */
object LineBeginParser : IMfmParser {
  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = when {
      input.length == startAt -> true
      startAt == 0 -> true
      CrParser.find(input, startAt - 1, context).success -> true
      LfParser.find(input, startAt - 1, context).success -> true
      else -> false
    }

    return if (result) {
      success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
    } else {
      failure()
    }
  }
}