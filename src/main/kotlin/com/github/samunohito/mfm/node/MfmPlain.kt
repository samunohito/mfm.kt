package com.github.samunohito.mfm.node

class MfmPlain(children: List<IMfmNode>) : IMfmNode, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Plain
}