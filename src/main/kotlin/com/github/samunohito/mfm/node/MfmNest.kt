package com.github.samunohito.mfm.node

import com.github.samunohito.mfm.MfmUtils

class MfmNest(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  override val type = MfmNodeType.Nest

  override fun stringify(): String {
    return MfmUtils.stringify(children)
  }
}