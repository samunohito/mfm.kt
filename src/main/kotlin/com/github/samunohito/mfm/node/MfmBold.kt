package com.github.samunohito.mfm.node

data class MfmBold(override val children: List<IMfmInline<*>>) : IMfmInline<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Bold
  override val props = MfmPropsEmpty

  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromNest(nest: MfmNest<*>): MfmBold {
      // 子要素はInline系しか受け付けない
      nest.children.forEach { require(it is IMfmInline) }

      return MfmBold(nest.children as List<IMfmInline<*>>)
    }
  }
}