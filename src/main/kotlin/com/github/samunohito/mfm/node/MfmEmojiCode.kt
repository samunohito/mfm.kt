package com.github.samunohito.mfm.node

class MfmEmojiCode(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmEmojiCode.Props> {
  override val type = MfmNodeType.EmojiCode

  constructor(name: String) : this(Props(name))

  class Props(val name: String) : IMfmProps
}