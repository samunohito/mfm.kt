package com.github.samunohito.mfm.node

interface IMfmNodeNestable : IMfmNode {
  val children: List<IMfmNode>

  fun addChild(node: IMfmNode) {
    return addChild(listOf(node))
  }

  fun addChild(nodes: Iterable<IMfmNode>)
}