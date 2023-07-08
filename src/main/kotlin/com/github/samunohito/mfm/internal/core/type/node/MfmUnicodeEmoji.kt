package com.github.samunohito.mfm.internal.core.type.node

data class MfmUnicodeEmoji(override val props: Props) : IMfmBlock<MfmUnicodeEmoji.Props> {
  override val type = MfmNodeType.UnicodeEmoji

  data class Props(val emoji: String) : IMfmProps
}