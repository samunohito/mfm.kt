package com.github.samunohito.mfm.node

class MfmUnicodeEmoji(
  override val props: Props
) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmUnicodeEmoji.Props> {
  override val type = MfmNodeType.UnicodeEmoji

  constructor(emoji: String) : this(Props(emoji))

  data class Props(val emoji: String) : IMfmProps
}