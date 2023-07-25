package com.github.samunohito.mfm.node

import com.github.samunohito.mfm.MfmUtils

class MfmBold(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())

  override val type = MfmNodeType.Bold

  override fun stringify(): String {
    return "**${MfmUtils.stringify(children)}**"
  }
}