package com.github.samunohito.mfm.node

data class MfmMathInline(override val props: Props) :
  IMfmBlock<MfmMathInline.Props> {
  override val type = MfmNodeType.MathInline

  data class Props(val formula: String) : IMfmProps
}