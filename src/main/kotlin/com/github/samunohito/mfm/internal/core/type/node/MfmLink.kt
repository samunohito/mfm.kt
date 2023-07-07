package com.github.samunohito.mfm.internal.core.type.node

class MfmUrl(override val props: Props) : IMfmBlock<MfmUrl.Props> {
  override val type = MfmNodeType.HashTag

  data class Props(val url: String, val brackets: Boolean?) : IMfmProps
}