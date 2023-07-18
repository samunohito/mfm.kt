package com.github.samunohito.mfm.node

data class MfmItalic(
  override val children: List<IMfmInline>
) : IMfmInline, IMfmNodeNestable<MfmItalic> {
  override val type = MfmNodeType.Italic

  override fun addChild(nodes: Iterable<IMfmNode>): MfmItalic {
    val filteredNodes = nodes.filterIsInstance<IMfmInline>()
    return copy(children = children + filteredNodes)
  }
}