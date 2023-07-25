package com.github.samunohito.mfm.node

class MfmEmojiCode(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmEmojiCode.Props> {
  constructor(name: String) : this(Props(name))

  override val type = MfmNodeType.EmojiCode

  override fun stringify(): String {
    return ":${props.name}:"
  }

  data class Props(var name: String) : IMfmProps
}