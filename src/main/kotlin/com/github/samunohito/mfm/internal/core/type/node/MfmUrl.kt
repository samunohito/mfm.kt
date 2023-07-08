package com.github.samunohito.mfm.internal.core.type.node

data class MfmUrl(override val props: Props) : IMfmBlock<MfmUrl.Props> {
  override val type = MfmNodeType.Url

  data class Props(val url: String, val brackets: Boolean?) : IMfmProps
}