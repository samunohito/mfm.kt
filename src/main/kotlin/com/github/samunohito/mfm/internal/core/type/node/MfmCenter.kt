package com.github.samunohito.mfm.internal.core.type.node

data class MfmQuote(override val children: List<IMfmNode<*>>) : IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Quote
  override val props = MfmPropsEmpty
}