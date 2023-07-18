package com.github.samunohito.mfm.node

data class MfmEmojiCode(
  override val props: Props
) : IMfmInline, IMfmSimpleNode, IMfmNodePropertyHolder<MfmEmojiCode.Props> {
  override val type = MfmNodeType.EmojiCode

  constructor(name: String) : this(Props(name))

  data class Props(val name: String) : IMfmProps
}