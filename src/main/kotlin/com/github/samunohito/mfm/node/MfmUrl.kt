package com.github.samunohito.mfm.node

data class MfmUrl(override val props: Props) : IMfmInline, IMfmNodePropertyHolder<MfmUrl.Props> {
  override val type = MfmNodeType.Url

  constructor(url: String, brackets: Boolean?) : this(Props(url, brackets))

  data class Props(val url: String, val brackets: Boolean?) : IMfmProps
}