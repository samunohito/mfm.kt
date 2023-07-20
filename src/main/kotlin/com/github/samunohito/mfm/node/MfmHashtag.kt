package com.github.samunohito.mfm.node

class MfmHashtag(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmHashtag.Props> {
  override val type = MfmNodeType.HashTag

  constructor(hashtag: String) : this(Props(hashtag))

  class Props(val hashtag: String) : IMfmProps
}