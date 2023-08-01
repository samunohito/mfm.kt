package com.github.samunohito.mfm.api.parser

import com.github.samunohito.mfm.api.parser.block.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.fixed.CharSequenceTerminateParser
import com.github.samunohito.mfm.api.parser.inline.*

/**
 * An [IMfmParser] implementation for detecting all syntax.
 */
class FullParser(
  terminateParser: IMfmParser = CharSequenceTerminateParser,
) : RecursiveParserBase(terminateParser) {
  override val parsers: List<IMfmParser>
    get() = _parsers
  override val foundType = FoundType.Full

  companion object {
    private val _parsers = listOf(
      UnicodeEmojiParser,
      CenterTagParser,
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
      CodeBlockParser,
      InlineCodeParser,
      QuoteParser,
      MathBlockParser,
      MathInlineParser,
      StrikeWaveParser,
      FnParser,
      MentionParser,
      HashtagParser,
      EmojiCodeParser,
      LinkParser,
      UrlParser,
      SearchParser,
    )
  }
}