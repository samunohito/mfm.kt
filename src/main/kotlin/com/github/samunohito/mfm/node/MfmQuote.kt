package com.github.samunohito.mfm.node

import com.github.samunohito.mfm.MfmUtils

class MfmQuote(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Quote

  constructor(vararg children: IMfmNode) : this(children.toList())

  override fun stringify(): String {
    return MfmUtils.stringify(children).prependIndent("> ")
  }
}