package com.github.samunohito.mfm.node

class MfmMathInline(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmMathInline.Props> {
  constructor(formula: String) : this(Props(formula))

  override val type = MfmNodeType.MathInline

  override fun stringify(): String {
    return "\\(${props.formula}\\)"
  }

  data class Props(var formula: String) : IMfmProps
}