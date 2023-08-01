package com.github.samunohito.mfm.api.parser.core.fixed

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.core.AlternateParser

/**
 * An implementation of [IMfmParser] that searches for newline characters (line feed, carriage return, and carriage return followed by line feed) in a given string
 */
object NewLineParser : IMfmParser {
  private val delegate = AlternateParser(CrParser, LfParser, CrLfParser)

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    return delegate.find(input, startAt, context)
  }
}