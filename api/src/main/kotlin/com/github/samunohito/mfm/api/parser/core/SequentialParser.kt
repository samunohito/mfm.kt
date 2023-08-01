package com.github.samunohito.mfm.api.parser.core

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.core.utils.ParserUtils

/**
 * An implementation of [IMfmParser] that sequentially searches for substrings using multiple other MFM parsers.
 * We expect the input string to satisfy all [IMfmParser] passed to [parsers], and if any one of the [parsers] fails, this implementation will also treat the search as failed.
 *
 * @param parsers A collection of [IMfmParser] to be used for searching.
 */
class SequentialParser(private val parsers: Collection<IMfmParser>) : IMfmParser {
  constructor(vararg parsers: IMfmParser) : this(parsers.toList())

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    return ParserUtils.sequential(input, startAt, parsers, context)
  }
}