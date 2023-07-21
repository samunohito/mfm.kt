package com.github.samunohito.mfm.node

class MfmQuote(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())
  override val type = MfmNodeType.Quote
}