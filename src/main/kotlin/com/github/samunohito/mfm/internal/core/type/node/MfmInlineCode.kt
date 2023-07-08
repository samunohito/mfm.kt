package com.github.samunohito.mfm.internal.core.type.node

data class MfmInlineCode(override val props: Props) : IMfmBlock<MfmInlineCode.Props> {
  override val type = MfmNodeType.InlineCode

  data class Props(val code: String) : IMfmProps
}