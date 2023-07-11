package com.github.samunohito.mfm.node

data class MfmItalic(override val children: List<IMfmInline<*>>) : IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Italic
  override val props = MfmPropsEmpty
}