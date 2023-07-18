package com.github.samunohito.mfm.node

data class MfmFn(
  override val props: Props,
  override val children: List<IMfmInline>
) : IMfmInline, IMfmNodePropertyHolder<MfmFn.Props>, IMfmNodeNestable<MfmFn> {
  override val type = MfmNodeType.Fn

  override fun addChild(nodes: Iterable<IMfmNode>): MfmFn {
    val filteredNodes = nodes.filterIsInstance<IMfmInline>()
    return copy(children = children + filteredNodes)
  }

  data class Props(val name: Boolean, val args: Map<String, Any>) : IMfmProps
}