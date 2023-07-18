package com.github.samunohito.mfm.node

data class MfmMathBlock(override val props: Props) : IMfmBlock, IMfmNodePropertyHolder<MfmMathBlock.Props> {
  override val type = MfmNodeType.MathBlock

  constructor(formula: String) : this(Props(formula))

  data class Props(val formula: String) : IMfmProps
}