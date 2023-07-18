package com.github.samunohito.mfm.node

interface IMfmNodeNestable<T : IMfmNodeNestable<T>> : IMfmNode {
  val children: List<IMfmNode>

  fun addChild(node: IMfmNode): T {
    return addChild(listOf(node))
  }

  fun addChild(nodes: Iterable<IMfmNode>): T
}