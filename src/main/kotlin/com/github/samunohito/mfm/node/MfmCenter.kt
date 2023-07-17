package com.github.samunohito.mfm.node

data class MfmCenter(
  override val children: List<IMfmInline<*>>
) : IMfmBlock<MfmPropsEmpty>, IMfmNodeNestable<MfmCenter> {
  override val type = MfmNodeType.Center
  override val props = MfmPropsEmpty

  override fun addChild(nodes: Iterable<IMfmNode<*>>): MfmCenter {
    val filteredNodes = nodes.filterIsInstance<IMfmInline<*>>()
    return copy(children = children + filteredNodes)
  }
}