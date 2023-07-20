package com.github.samunohito.mfm.node

class MfmSearch(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmSearch.Props> {
  override val type = MfmNodeType.Search

  constructor(query: String, content: String) : this(Props(query, content))

  class Props(val query: String, val content: String) : IMfmProps
}