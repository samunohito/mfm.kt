package com.github.samunohito.mfm.api.parser.core

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.utils.next

/**
 * An implementation of [IMfmParser] that searches for a given string in a given string.
 */
class StringParser(private val word: String) : IMfmParser {
  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
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