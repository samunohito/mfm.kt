package com.github.samunohito.mfm.node

class MfmMathBlock(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmMathBlock.Props> {
  constructor(formula: String) : this(Props(formula))

  override val type = MfmNodeType.MathBlock

  override fun stringify(): String {
    return "\\[\n${props.formula}\n\\]"
  }

  data class Props(val formula: String) : IMfmProps
}