package com.github.samunohito.mfm.node

class MfmHashtag(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmHashtag.Props> {
  override val type = MfmNodeType.HashTag

  constructor(hashtag: String) : this(Props(hashtag))

  data class Props(val hashtag: String) : IMfmProps
}