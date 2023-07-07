package com.github.samunohito.mfm.internal.core.type.node

class MfmQuote(override val children: List<IMfmNode<*>>) : IMfmBlock<MfmQuote.MfmQuoteProps>, IMfmIncludeChildren {
  override val type = MfmNodeType.Quote
  override val props = MfmQuoteProps()

  class MfmQuoteProps : IMfmProps
}