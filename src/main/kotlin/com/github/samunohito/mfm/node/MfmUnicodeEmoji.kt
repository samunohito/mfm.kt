package com.github.samunohito.mfm.node

class MfmUnicodeEmoji(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmUnicodeEmoji.Props> {
  override val type = MfmNodeType.UnicodeEmoji

  constructor(emoji: String) : this(Props(emoji))

  class Props(val emoji: String) : IMfmProps
}