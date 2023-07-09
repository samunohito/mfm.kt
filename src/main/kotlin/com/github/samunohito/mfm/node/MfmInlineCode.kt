package com.github.samunohito.mfm.node

data class MfmInlineCode(override val props: Props) :
  IMfmBlock<MfmInlineCode.Props> {
  override val type = MfmNodeType.InlineCode

  data class Props(val code: String) : IMfmProps
}