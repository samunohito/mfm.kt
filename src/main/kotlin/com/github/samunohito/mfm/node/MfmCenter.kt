package com.github.samunohito.mfm.node

class MfmCenter(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Center
}