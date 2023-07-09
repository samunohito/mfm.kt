package com.github.samunohito.mfm.node

data class MfmHashtag(override val props: Props) : IMfmBlock<MfmHashtag.Props> {
  override val type = MfmNodeType.HashTag

  data class Props(val hashtag: String) : IMfmProps
}