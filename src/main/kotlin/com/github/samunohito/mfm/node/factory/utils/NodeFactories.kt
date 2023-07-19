package com.github.samunohito.mfm.node.factory.utils

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.factory.*

object NodeFactories {
  fun get(foundType: FoundType): INodeFactory<*> {
    return when (foundType) {
      FoundType.Big -> BigNodeFactory()
      FoundType.BoldAsta -> BoldAstaNodeFactory()
      FoundType.BoldTag -> BoldTagNodeFactory()
      FoundType.BoldUnder -> BoldUnderNodeFactory()
      FoundType.CenterTag -> CenterTagNodeFactory()
      FoundType.CodeBlock -> CodeBlockNodeFactory()
      FoundType.EmojiCode -> EmojiCodeNodeFactory()
      FoundType.Fn -> FnNodeFactory()
      FoundType.Hashtag -> HashtagNodeFactory()
      FoundType.InlineCode -> InlineCodeNodeFactory()
      FoundType.ItalicAsta -> ItalicAstaNodeFactory()
      FoundType.ItalicTag -> ItalicTagNodeFactory()
      FoundType.ItalicUnder -> ItalicUnderNodeFactory()
      FoundType.Link -> LinkNodeFactory()
      FoundType.MathBlock -> MathBlockNodeFactory()
      FoundType.MathInline -> MathInlineNodeFactory()
      FoundType.Mention -> MentionNodeFactory()
      FoundType.PlainTag -> PlainTagNodeFactory()
      FoundType.Quote -> QuoteNodeFactory()
      FoundType.Search -> SearchNodeFactory()
      FoundType.SmallTag -> SmallTagNodeFactory()
      FoundType.StrikeTag -> StrikeTagNodeFactory()
      FoundType.StrikeWave -> StrikeWaveNodeFactory()
      FoundType.UnicodeEmoji -> UnicodeEmojiNodeFactory()
      FoundType.Url -> UrlNodeFactory()
      FoundType.UrlAlt -> UrlAltNodeFactory()
      FoundType.Text -> TextNodeFactory()
      else -> throw IllegalArgumentException("Not Support FoundType: $foundType")
    }
  }
}