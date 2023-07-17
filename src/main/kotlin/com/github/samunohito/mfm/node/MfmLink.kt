package com.github.samunohito.mfm.node

data class MfmLink(
  override val props: Props,
  override val children: List<IMfmInline<*>>
) : IMfmInline<MfmLink.Props>, IMfmNodeNestable<MfmLink> {
  override val type = MfmNodeType.Link

  constructor(silent: Boolean, url: String, children: List<IMfmInline<*>>) : this(Props(silent, url), children)

  override fun addChild(nodes: Iterable<IMfmNode<*>>): MfmLink {
    val filteredNodes = nodes.filterIsInstance<IMfmInline<*>>()
    return copy(children = children + filteredNodes)
  }

  data class Props(val silent: Boolean, val url: String) : IMfmProps
}