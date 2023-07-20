package com.github.samunohito.mfm.node

class MfmMathInline(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmMathInline.Props> {
  override val type = MfmNodeType.MathInline

  constructor(formula: String) : this(Props(formula))

  data class Props(val formula: String) : IMfmProps
}