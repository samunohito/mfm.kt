package com.github.samunohito.mfm.node

class MfmInlineCode(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmInlineCode.Props> {
  override val type = MfmNodeType.InlineCode

  constructor(code: String) : this(Props(code))

  data class Props(val code: String) : IMfmProps
}