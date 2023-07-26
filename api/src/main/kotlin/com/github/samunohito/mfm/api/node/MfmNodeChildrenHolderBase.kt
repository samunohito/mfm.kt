package com.github.samunohito.mfm.api.node

abstract class MfmNodeChildrenHolderBase(
  children: List<IMfmNode> = listOf()
) : MfmNodeBase(), IMfmNodeChildrenHolder {
  override val children: List<IMfmNode> = mutableListOf<IMfmNode>().apply { addAll(children) }

  override fun addChild(nodes: Iterable<IMfmNode>) {
    (children as MutableList).addAll(nodes)
  }
}