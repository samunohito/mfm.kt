package com.github.samunohito.mfm.node

class MfmQuote(children: List<IMfmNode>) : IMfmNode, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Quote
}