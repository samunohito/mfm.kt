package com.github.samunohito.mfm.node

data class MfmNest(
  override val children: List<IMfmNode<*>>
) : IMfmInline<MfmPropsEmpty>, IMfmBlock<MfmPropsEmpty>, IMfmNodeNestable<MfmNest> {
  override val type = MfmNodeType.Nest
  override val props = MfmPropsEmpty
  override fun addChild(nodes: Iterable<IMfmNode<*>>): MfmNest {
    return copy(children = children + nodes)
  }
}