package com.github.samunohito.mfm.node

data class MfmSmall(
  override val children: List<IMfmInline>
) : IMfmInline, IMfmNodeNestable<MfmSmall> {
  override val type = MfmNodeType.Small

  constructor(vararg children: IMfmInline) : this(children.toList())

  override fun addChild(nodes: Iterable<IMfmNode>): MfmSmall {
    val filteredNodes = nodes.filterIsInstance<IMfmInline>()
    return copy(children = children + filteredNodes)
  }
}