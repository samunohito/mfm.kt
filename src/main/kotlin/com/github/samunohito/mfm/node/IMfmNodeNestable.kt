package com.github.samunohito.mfm.node

interface IMfmNodeNestable<TProps : IMfmProps, TThis : IMfmNodeNestable<TProps, TThis>> : IMfmNode<TProps> {
  val children: List<IMfmNode<*>>

  fun addChild(node: IMfmNode<*>): TThis {
    return addChild(listOf(node))
  }

  fun addChild(nodes: Iterable<IMfmNode<*>>): TThis
}