package com.github.samunohito.mfm.node

data class MfmText(override val props: Props) : IMfmInline, IMfmNodePropertyHolder<MfmText.Props> {
  override val type = MfmNodeType.Text

  constructor(text: String) : this(Props(text))

  data class Props(val text: String) : IMfmProps
}