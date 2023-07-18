package com.github.samunohito.mfm.node

data class MfmBlockCode(override val props: Props) : IMfmBlock, IMfmNodePropertyHolder<MfmBlockCode.Props> {
  override val type = MfmNodeType.BlockCode

  constructor(code: String, lang: String? = null) : this(Props(code, lang))

  data class Props(val code: String, val lang: String?) : IMfmProps
}