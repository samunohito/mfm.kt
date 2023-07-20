package com.github.samunohito.mfm.node

class MfmPlain(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Plain
}