package com.github.samunohito.mfm.node

class MfmStrike(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Strike
}