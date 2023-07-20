package com.github.samunohito.mfm.node

class MfmItalic(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Italic
}