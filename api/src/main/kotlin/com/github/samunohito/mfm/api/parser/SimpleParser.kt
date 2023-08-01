package com.github.samunohito.mfm.api.parser

import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.fixed.CharSequenceTerminateParser
import com.github.samunohito.mfm.api.parser.inline.EmojiCodeParser
import com.github.samunohito.mfm.api.parser.inline.UnicodeEmojiParser

class SimpleParser(
  terminateParser: IMfmParser = CharSequenceTerminateParser,
) : RecursiveParserBase(terminateParser) {
  override val parsers: List<IMfmParser>
    get() = _parsers
  override val foundType = FoundType.Simple

  companion object {
    private val _parsers = listOf(
      UnicodeEmojiParser,
      EmojiCodeParser,
    )
  }
}