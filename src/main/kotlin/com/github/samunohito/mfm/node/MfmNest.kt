package com.github.samunohito.mfm.node

class MfmNest(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Nest
}