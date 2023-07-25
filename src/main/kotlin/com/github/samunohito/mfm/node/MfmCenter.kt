package com.github.samunohito.mfm.node

import com.github.samunohito.mfm.MfmUtils

class MfmCenter(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())

  override val type = MfmNodeType.Center

  override fun stringify(): String {
    return "<center>\n${MfmUtils.stringify(children)}\n</center>"
  }
}