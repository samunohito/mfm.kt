package com.github.samunohito.mfm.node

class MfmSmall(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Small
}