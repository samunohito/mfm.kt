package com.github.samunohito.mfm.node

data class MfmQuote(override val children: List<IMfmNode<*>>) :
  IMfmBlock<MfmPropsEmpty>,
  IMfmIncludeChildren {
  override val type = MfmNodeType.Quote
  override val props = MfmPropsEmpty
}