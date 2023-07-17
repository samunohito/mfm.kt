package com.github.samunohito.mfm.node

data class MfmFn(
  override val props: Props,
  override val children: List<IMfmInline<*>>
) : IMfmInline<MfmFn.Props>, IMfmNodeNestable<MfmFn> {
  override val type = MfmNodeType.Fn

  data class Props(val name: Boolean, val args: Map<String, Any>) : IMfmProps

  override fun addChild(nodes: Iterable<IMfmNode<*>>): MfmFn {
    val filteredNodes = nodes.filterIsInstance<IMfmInline<*>>()
    return copy(children = children + filteredNodes)
  }
}