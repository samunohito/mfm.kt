package com.github.samunohito.mfm.node

class MfmInlineCode(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmInlineCode.Props> {
  constructor(code: String) : this(Props(code))

  override val type = MfmNodeType.InlineCode

  override fun stringify(): String {
    return "`${props.code}`"
  }

  data class Props(val code: String) : IMfmProps
}