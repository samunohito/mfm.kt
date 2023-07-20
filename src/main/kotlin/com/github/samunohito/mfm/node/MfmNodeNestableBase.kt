package com.github.samunohito.mfm.node

abstract class MfmNodeNestableBase(children: List<IMfmNode> = listOf()) : IMfmNodeNestable {
  private val _children = mutableListOf<IMfmNode>().apply { addAll(children) }

  override val children: List<IMfmNode>
    get() = _children

  override fun addChild(nodes: Iterable<IMfmNode>) {
    _children.addAll(nodes)
  }
}