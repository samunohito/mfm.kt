package com.github.samunohito.mfm.node

class MfmStrike(children: List<IMfmNode>) : IMfmNode, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Strike
}