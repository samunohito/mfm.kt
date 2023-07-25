package com.github.samunohito.mfm.api.node

abstract class MfmNodeChildrenHolderBase(
  children: List<IMfmNode> = listOf()
) : MfmNodeBase(), IMfmNodeChildrenHolder {
  private val _children = mutableListOf<IMfmNode>().apply { addAll(children) }

  override val children: List<IMfmNode>
    get() = _children

  override fun addChild(nodes: Iterable<IMfmNode>) {
    _children.addAll(nodes)
  }
}