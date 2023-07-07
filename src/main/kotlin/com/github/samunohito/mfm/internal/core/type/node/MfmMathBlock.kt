package com.github.samunohito.mfm.internal.core.type.node

class MfmMthBlock(override val props: Props) : IMfmBlock<MfmMthBlock.Props> {
  override val type = MfmNodeType.MathBlock

  data class Props(val formula: String) : IMfmProps
}