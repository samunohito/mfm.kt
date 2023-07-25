package com.github.samunohito.mfm.api.node

interface IMfmNodeChildrenHolder : IMfmNode {
  val children: List<IMfmNode>

  fun addChild(node: IMfmNode) {
    return addChild(listOf(node))
  }

  fun addChild(nodes: Iterable<IMfmNode>)
}