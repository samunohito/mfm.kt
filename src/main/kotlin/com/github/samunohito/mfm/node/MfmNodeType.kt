package com.github.samunohito.mfm.node

enum class MfmNodeType(val attributes: Set<MfmNodeAttribute>) {
  Quote(setOf(MfmNodeAttribute.Block)),
  Search(setOf(MfmNodeAttribute.Block)),
  BlockCode(setOf(MfmNodeAttribute.Block)),
  MathBlock(setOf(MfmNodeAttribute.Block)),
  Center(setOf(MfmNodeAttribute.Block)),
  UnicodeEmoji(setOf(MfmNodeAttribute.Inline, MfmNodeAttribute.Simple)),
  EmojiCode(setOf(MfmNodeAttribute.Inline, MfmNodeAttribute.Simple)),
  Bold(setOf(MfmNodeAttribute.Inline)),
  Small(setOf(MfmNodeAttribute.Inline)),
  Italic(setOf(MfmNodeAttribute.Inline)),
  Strike(setOf(MfmNodeAttribute.Inline)),
  InlineCode(setOf(MfmNodeAttribute.Inline)),
  MathInline(setOf(MfmNodeAttribute.Inline)),
  Mention(setOf(MfmNodeAttribute.Inline)),
  HashTag(setOf(MfmNodeAttribute.Inline)),
  Url(setOf(MfmNodeAttribute.Inline)),
  Link(setOf(MfmNodeAttribute.Inline)),
  Fn(setOf(MfmNodeAttribute.Inline)),
  Plain(setOf(MfmNodeAttribute.Inline)),
  Text(setOf(MfmNodeAttribute.Inline, MfmNodeAttribute.Simple)),
  Nest(setOf(MfmNodeAttribute.Virtuality)),
  ;

  companion object {
    val blockTypes = values().filter { it.attributes.contains(MfmNodeAttribute.Block) }.toSet()
    val inlineTypes = values().filter { it.attributes.contains(MfmNodeAttribute.Inline) }.toSet()
    val simpleTypes = values().filter { it.attributes.contains(MfmNodeAttribute.Simple) }.toSet()
    val virtualTypes = values().filter { it.attributes.contains(MfmNodeAttribute.Virtuality) }.toSet()
  }
}