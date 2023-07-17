package com.github.samunohito.mfm.node

data class MfmPlain(
  override val children: List<MfmText>
) : IMfmInline<MfmPropsEmpty>, IMfmNodeNestable<MfmPlain> {
  override val type = MfmNodeType.Plain
  override val props = MfmPropsEmpty
  override fun addChild(nodes: Iterable<IMfmNode<*>>): MfmPlain {
    val filteredNodes = nodes.filterIsInstance<MfmText>()
    return copy(children = children + filteredNodes)
  }
}