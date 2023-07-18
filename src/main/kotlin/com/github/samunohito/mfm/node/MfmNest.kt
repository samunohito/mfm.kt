package com.github.samunohito.mfm.node

data class MfmNest(
  override val children: List<IMfmNode>
) : IMfmInline, IMfmBlock, IMfmNodeNestable<MfmNest> {
  override val type = MfmNodeType.Nest

  constructor() : this(listOf())

  override fun addChild(nodes: Iterable<IMfmNode>): MfmNest {
    return copy(children = children + nodes)
  }
}