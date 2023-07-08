package com.github.samunohito.mfm.internal.core.type.node

data class MfmBlockCode(override val props: Props) : IMfmBlock<MfmBlockCode.Props> {
  override val type = MfmNodeType.BlockCode

  data class Props(val code: String, val lang: String?) : IMfmProps
}