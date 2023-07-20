package com.github.samunohito.mfm.node

class MfmBlockCode(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmBlockCode.Props> {
  override val type = MfmNodeType.BlockCode

  constructor(code: String, lang: String? = null) : this(Props(code, lang))

  class Props(val code: String, val lang: String?) : IMfmProps
}