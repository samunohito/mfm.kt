package com.github.samunohito.mfm.internal.core.type.node

class MfmHashtag(override val props: Props) : IMfmBlock<MfmHashtag.Props> {
  override val type = MfmNodeType.HashTag

  data class Props(val hashtag: String) : IMfmProps
}