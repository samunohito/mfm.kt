package com.github.samunohito.mfm.node

class MfmSmall(children: List<IMfmNode>) : IMfmNode, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Small
}