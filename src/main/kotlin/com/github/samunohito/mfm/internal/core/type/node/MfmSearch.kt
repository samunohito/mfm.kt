package com.github.samunohito.mfm.internal.core.type.node

data class MfmSearch(override val props: Props) : IMfmBlock<MfmSearch.Props> {
  override val type = MfmNodeType.Search

  data class Props(val query: String, val content: String) : IMfmProps
}