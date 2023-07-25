package com.github.samunohito.mfm.node

class MfmSearch(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmSearch.Props> {
  constructor(query: String, content: String) : this(Props(query, content))

  override val type = MfmNodeType.Search

  override fun stringify(): String {
    return props.content
  }

  data class Props(var query: String, var content: String) : IMfmProps
}