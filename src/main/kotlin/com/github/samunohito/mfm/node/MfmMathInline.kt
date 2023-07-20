package com.github.samunohito.mfm.node

class MfmMathInline(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmMathInline.Props> {
  override val type = MfmNodeType.MathInline

  constructor(formula: String) : this(Props(formula))

  class Props(val formula: String) : IMfmProps
}