package com.github.samunohito.mfm.node

class MfmCenter(children: List<IMfmNode>) : IMfmNode, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Center
}