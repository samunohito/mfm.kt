package com.github.samunohito.mfm.api.node

class MfmInlineCode(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmInlineCode.Props> {
  constructor(code: String) : this(Props(code))

  override val type = MfmNodeType.InlineCode

  override fun stringify(): String {
    return "`${props.code}`"
  }

  data class Props(var code: String) : IMfmProps
}