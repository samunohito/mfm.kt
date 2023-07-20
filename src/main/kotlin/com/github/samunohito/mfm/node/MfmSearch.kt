package com.github.samunohito.mfm.node

class MfmSearch(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmSearch.Props> {
  override val type = MfmNodeType.Search

  constructor(query: String, content: String) : this(Props(query, content))

  data class Props(val query: String, val content: String) : IMfmProps
}