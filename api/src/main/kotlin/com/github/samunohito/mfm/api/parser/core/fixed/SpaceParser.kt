package com.github.samunohito.mfm.api.parser.core.fixed

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.core.RegexParser

/**
 * An implementation of [IMfmParser] that searches for whitespace characters (space, tab, and full-width space) in a given string.
 */
object SpaceParser : IMfmParser {
  private val delegate = RegexParser(Regex("[\u0020\u3000\t]"))

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    return delegate.find(input, startAt, context)
  }
}