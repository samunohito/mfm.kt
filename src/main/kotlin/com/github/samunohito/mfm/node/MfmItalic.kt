package com.github.samunohito.mfm.node

data class MfmItalic(override val children: List<IMfmInline<*>>) : IMfmInline<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Italic
  override val props = MfmPropsEmpty
}