package com.github.samunohito.mfm.node

data class MfmUnicodeEmoji(override val props: Props) : IMfmInline<MfmUnicodeEmoji.Props> {
  override val type = MfmNodeType.UnicodeEmoji

  data class Props(val emoji: String) : IMfmProps
}