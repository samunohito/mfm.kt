package com.github.samunohito.mfm.node

class MfmInlineCode(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmInlineCode.Props> {
  override val type = MfmNodeType.InlineCode

  constructor(code: String) : this(Props(code))

  class Props(val code: String) : IMfmProps
}