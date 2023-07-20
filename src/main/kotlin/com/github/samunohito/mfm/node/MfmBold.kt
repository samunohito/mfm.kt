package com.github.samunohito.mfm.node

class MfmBold(children: List<IMfmNode>) : IMfmNode, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Bold
}