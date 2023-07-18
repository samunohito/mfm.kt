package com.github.samunohito.mfm.node

data class MfmFn(
  override val props: Props,
  override val children: List<IMfmInline>
) : IMfmInline, IMfmNodePropertyHolder<MfmFn.Props>, IMfmNodeNestable<MfmFn> {
  override val type = MfmNodeType.Fn

  constructor(name: String, args: Map<String, Any>, children: List<IMfmInline>) : this(Props(name, args), children)

  override fun addChild(nodes: Iterable<IMfmNode>): MfmFn {
    val filteredNodes = nodes.filterIsInstance<IMfmInline>()
    return copy(children = children + filteredNodes)
  }

  data class Props(val name: String, val args: Map<String, Any>) : IMfmProps
}