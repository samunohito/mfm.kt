package com.github.samunohito.mfm.node

class MfmHashtag(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmHashtag.Props> {
  constructor(hashtag: String) : this(Props(hashtag))

  override fun stringify(): String {
    return "#${props.hashtag}"
  }

  override val type = MfmNodeType.HashTag

  data class Props(val hashtag: String) : IMfmProps
}