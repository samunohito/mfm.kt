package com.github.samunohito.mfm.api.node

class MfmText(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmText.Props> {
  constructor(text: String) : this(Props(text))

  override val type = MfmNodeType.Text
  override fun stringify(): String {
    return props.text
  }

  data class Props(var text: String) : IMfmProps
}