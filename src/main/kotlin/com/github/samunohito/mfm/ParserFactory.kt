package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.FoundType

object ParserFactory {
  fun get(foundType: FoundType): IParser<*> {
    return when (foundType) {
      FoundType.Big -> BigParser()
      FoundType.BoldAsta -> BoldAstaParser()
      FoundType.BoldTag -> BoldTagParser()
      FoundType.BoldUnder -> BoldUnderParser()
      FoundType.CenterTag -> CenterTagParser()
      FoundType.CodeBlock -> CodeBlockParser()
      FoundType.EmojiCode -> EmojiCodeParser()
      FoundType.Fn -> FnParser()
      FoundType.Full -> FullParser()
      FoundType.Hashtag -> HashtagParser()
      FoundType.InlineCode -> InlineCodeParser()
      FoundType.Inline -> InlineParser()
      FoundType.ItalicAsta -> ItalicAstaParser()
      FoundType.ItalicTag -> ItalicTagParser()
      FoundType.ItalicUnder -> ItalicUnderParser()
      FoundType.Link -> LinkParser()
      FoundType.MathBlock -> MathBlockParser()
      FoundType.MathInline -> MathInlineParser()
      FoundType.Mention -> MentionParser()
      FoundType.PlainTag -> PlainTagParser()
      FoundType.Quote -> QuoteParser()
      FoundType.Search -> SearchParser()
      FoundType.Simple -> SimpleParser()
      FoundType.SmallTag -> SmallTagParser()
      FoundType.StrikeTag -> StrikeTagParser()
      FoundType.StrikeWave -> StrikeWaveParser()
      FoundType.UnicodeEmoji -> UnicodeEmojiParser()
      FoundType.Url -> UrlParser()
      FoundType.UrlAlt -> UrlAltParser()
      FoundType.Text -> TextParser()
      else -> throw IllegalArgumentException("Not Support FoundType: $foundType")
    }
  }
}