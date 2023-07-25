package com.github.samunohito.mfm.api.node

import com.github.samunohito.mfm.api.MfmUtils

class MfmSmall(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())

  override val type = MfmNodeType.Small

  override fun stringify(): String {
    return "<small>${MfmUtils.stringify(children)}</small>"
  }
}