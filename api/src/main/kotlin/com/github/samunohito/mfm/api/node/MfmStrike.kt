package com.github.samunohito.mfm.api.node

import com.github.samunohito.mfm.api.MfmUtils

class MfmStrike(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())

  override val type = MfmNodeType.Strike

  override fun stringify(): String {
    return "~~${MfmUtils.stringify(children)}~~"
  }
}