package com.github.samunohito.mfm.node

import com.github.samunohito.mfm.MfmUtils

class MfmPlain(children: List<IMfmNode>) : MfmNodeChildrenHolderBase(children), IMfmNode {
  constructor(vararg children: IMfmNode) : this(children.toList())
  constructor(vararg children: String) : this(children.map { MfmText(it) }.toList())

  override val type = MfmNodeType.Plain
  override fun stringify(): String {
    return "<plain>\n${MfmUtils.stringify(children)}\n</plain>"
  }
}