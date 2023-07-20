package com.github.samunohito.mfm.node

class MfmText(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmText.Props> {
  override val type = MfmNodeType.Text

  constructor(text: String) : this(Props(text))

  class Props(val text: String) : IMfmProps
}