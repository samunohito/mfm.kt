package com.github.samunohito.mfm.api.parser.core

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.core.utils.ParserUtils

/**
 * An implementation of [IMfmParser] that searches for a given string in a given string.
 * The input string is expected to satisfy any [IMfmParser] passed to [parsers]. If all [parsers] fail, this implementation treats the search as failing as well.
 *
 * @param parsers A collection of [IMfmParser] to be used for searching.
 */
class AlternateParser(private val parsers: Collection<IMfmParser>) : IMfmParser {
  constructor(vararg parsers: IMfmParser) : this(parsers.toList())

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    return ParserUtils.alternate(input, startAt, parsers, context)
  }
}