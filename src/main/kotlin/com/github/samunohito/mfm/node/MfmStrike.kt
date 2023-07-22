package com.github.samunohito.mfm.node

class MfmStrike(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())
  override val type = MfmNodeType.Strike
}