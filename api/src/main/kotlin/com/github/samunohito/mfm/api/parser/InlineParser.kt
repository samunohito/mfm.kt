package com.github.samunohito.mfm.api.parser

import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.fixed.CharSequenceTerminateParser
import com.github.samunohito.mfm.api.parser.inline.*

/**
 * An [IMfmParser] implementation for detecting inline syntax.
 */
class InlineParser(
  terminateParser: IMfmParser = CharSequenceTerminateParser,
) : RecursiveParserBase(terminateParser) {
  override val parsers: List<IMfmParser>
    get() = _parsers
  override val foundType = FoundType.Inline

  companion object {
    private val _parsers = listOf(
      UnicodeEmojiParser,
      SmallTagParser,
      PlainTagParser,
      BoldTagParser,
      ItalicTagParser,
      StrikeTagParser,
      UrlAltParser,
      BigParser,
      BoldAstaParser,
      ItalicAstaParser,
      BoldUnderParser,
      ItalicUnderParser,
      InlineCodeParser,
      MathInlineParser,
      StrikeWaveParser,
      FnParser,
      MentionParser,
      HashtagParser,
      EmojiCodeParser,
      LinkParser,
      UrlParser,
    )
  }
}