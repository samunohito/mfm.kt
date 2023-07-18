package com.github.samunohito.mfm.node

data class MfmBold(
  override val children: List<IMfmInline>
) : IMfmInline, IMfmNodeNestable<MfmBold> {
  override val type = MfmNodeType.Bold

  override fun addChild(nodes: Iterable<IMfmNode>): MfmBold {
    val filteredNodes = nodes.filterIsInstance<IMfmInline>()
    return copy(children = children + filteredNodes)
  }
}