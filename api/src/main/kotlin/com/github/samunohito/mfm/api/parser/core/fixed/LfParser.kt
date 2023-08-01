package com.github.samunohito.mfm.api.parser.core.fixed

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.core.StringParser

/**
 * An implementation of [IMfmParser] that searches for line feed characters in a given string.
 */
object LfParser : IMfmParser {
  private val delegate = StringParser("\n")

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    return delegate.find(input, startAt, context)
  }
}