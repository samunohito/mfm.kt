package com.github.samunohito.mfm.api.node

class MfmBlockCode(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmBlockCode.Props> {
  constructor(code: String, lang: String? = null) : this(Props(code, lang))

  override val type = MfmNodeType.BlockCode

  override fun stringify(): String {
    return "```${props.lang ?: ""}\n${props.code}\n```"
  }

  data class Props(var code: String, var lang: String?) : IMfmProps
}