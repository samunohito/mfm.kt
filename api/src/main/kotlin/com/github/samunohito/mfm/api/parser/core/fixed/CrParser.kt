package com.github.samunohito.mfm.api.parser.core.fixed

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.core.StringParser

/**
 * An implementation of [IMfmParser] that searches for carriage return characters in a given string.
 */
object CrParser : IMfmParser {
  private val delegate = StringParser("\r")

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    return delegate.find(input, startAt, context)
  }
}