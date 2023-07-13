package com.github.samunohito.mfm.node

data class MfmBold(override val children: List<IMfmInline<*>>) : IMfmInline<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Bold
  override val props = MfmPropsEmpty
}