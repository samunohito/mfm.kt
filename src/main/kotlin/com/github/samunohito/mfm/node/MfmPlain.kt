package com.github.samunohito.mfm.node

class MfmPlain(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())
  constructor(vararg children: String) : this(children.map { MfmText(it) }.toList())
  override val type = MfmNodeType.Plain
}