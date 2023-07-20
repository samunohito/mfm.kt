package com.github.samunohito.mfm.node

class MfmQuote(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Quote
}