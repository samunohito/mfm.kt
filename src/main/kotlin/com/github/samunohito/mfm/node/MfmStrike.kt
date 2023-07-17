package com.github.samunohito.mfm.node

data class MfmStrike(
  override val children: List<IMfmInline<*>>
) : IMfmInline<MfmPropsEmpty>, IMfmNodeNestable<MfmStrike> {
  override val type = MfmNodeType.Strike
  override val props = MfmPropsEmpty
  override fun addChild(nodes: Iterable<IMfmNode<*>>): MfmStrike {
    val filteredNodes = nodes.filterIsInstance<IMfmInline<*>>()
    return copy(children = children + filteredNodes)
  }
}