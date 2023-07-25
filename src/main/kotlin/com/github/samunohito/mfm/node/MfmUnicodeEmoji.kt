package com.github.samunohito.mfm.node

class MfmUnicodeEmoji(
  override val props: Props
) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmUnicodeEmoji.Props> {
  constructor(emoji: String) : this(Props(emoji))

  override val type = MfmNodeType.UnicodeEmoji

  override fun stringify(): String {
    return props.emoji
  }

  data class Props(val emoji: String) : IMfmProps
}