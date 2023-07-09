package com.github.samunohito.mfm.node

data class MfmMathBlock(override val props: Props) :
  IMfmBlock<MfmMathBlock.Props> {
  override val type = MfmNodeType.MathBlock

  data class Props(val formula: String) : IMfmProps
}