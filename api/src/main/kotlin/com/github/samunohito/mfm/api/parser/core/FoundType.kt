package com.github.samunohito.mfm.api.parser.core

import com.github.samunohito.mfm.api.parser.IMfmParser

/**
 * Indicates the type of format found by [IMfmParser]
 */

enum class FoundType {
  Unknown,
  Empty,
  Core,
  Big,
  BoldAsta,
  BoldTag,
  BoldUnder,
  CenterTag,
  CodeBlock,
  EmojiCode,
  Fn,
  Full,
  Hashtag,
  InlineCode,
  Inline,
  ItalicAsta,
  ItalicTag,
  ItalicUnder,
  Link,
  MathBlock,
  MathInline,
  Mention,
  PlainTag,
  Quote,
  Search,
  Simple,
  SmallTag,
  StrikeTag,
  StrikeWave,
  UnicodeEmoji,
  Url,
  UrlAlt,
  Text,
}