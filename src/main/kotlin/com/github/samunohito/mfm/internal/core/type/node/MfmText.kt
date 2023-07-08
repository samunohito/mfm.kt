package com.github.samunohito.mfm.internal.core.type.node

data class MfmText(override val props: Props) : IMfmBlock<MfmText.Props> {
  override val type = MfmNodeType.Text

  constructor(text: String) : this(Props(text))

  data class Props(val text: String) : IMfmProps
}