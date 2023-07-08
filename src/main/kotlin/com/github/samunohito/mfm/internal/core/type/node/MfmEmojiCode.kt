package com.github.samunohito.mfm.internal.core.type.node

data class MfmEmojiCode(override val props: Props) : IMfmBlock<MfmEmojiCode.Props> {
  override val type = MfmNodeType.EmojiCode

  data class Props(val name: String) : IMfmProps
}