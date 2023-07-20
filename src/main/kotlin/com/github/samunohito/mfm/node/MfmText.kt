package com.github.samunohito.mfm.node

class MfmText(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmText.Props> {
  override val type = MfmNodeType.Text

  constructor(text: String) : this(Props(text))

  data class Props(val text: String) : IMfmProps
}