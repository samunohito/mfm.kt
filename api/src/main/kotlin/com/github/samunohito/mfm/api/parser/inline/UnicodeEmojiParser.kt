package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.failure

/**
 * An [IMfmParser] implementation for detecting "Unicode Emoji" syntax.
 * Emojis defined in Unicode are search results.
 */
object UnicodeEmojiParser : IMfmParser {
  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    // TODO: Implement
    return failure()
  }
}