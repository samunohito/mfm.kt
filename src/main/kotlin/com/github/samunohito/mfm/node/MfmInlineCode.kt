package com.github.samunohito.mfm.node

data class MfmInlineCode(override val props: Props) : IMfmInline, IMfmNodePropertyHolder<MfmInlineCode.Props> {
  override val type = MfmNodeType.InlineCode

  constructor(code: String) : this(Props(code))

  data class Props(val code: String) : IMfmProps
}