package com.github.samunohito.mfm.node

class MfmBold(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Bold
}