package com.github.samunohito.mfm.api.node

import com.github.samunohito.mfm.api.MfmUtils

class MfmItalic(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())

  override val type = MfmNodeType.Italic

  override fun stringify(): String {
    return "<i>${MfmUtils.stringify(children)}</i>"
  }
}