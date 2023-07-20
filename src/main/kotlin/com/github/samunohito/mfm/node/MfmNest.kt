package com.github.samunohito.mfm.node

class MfmNest(children: List<IMfmNode>) : IMfmNode, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Nest
}