package com.github.samunohito.mfm.internal.core.type.node

class MfmCodeBlock(override val props: Props) : IMfmBlock<MfmCodeBlock.Props> {
  override val type = MfmNodeType.BlockCode

  data class Props(val code: String, val lang: String?) : IMfmProps
}