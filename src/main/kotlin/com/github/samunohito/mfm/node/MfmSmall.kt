package com.github.samunohito.mfm.node

data class MfmSmall(
  override val children: List<IMfmInline<*>>
) : IMfmInline<MfmPropsEmpty>, IMfmNodeNestable<MfmSmall> {
  override val type = MfmNodeType.Small
  override val props = MfmPropsEmpty
  override fun addChild(nodes: Iterable<IMfmNode<*>>): MfmSmall {
    val filteredNodes = nodes.filterIsInstance<IMfmInline<*>>()
    return copy(children = children + filteredNodes)
  }
}