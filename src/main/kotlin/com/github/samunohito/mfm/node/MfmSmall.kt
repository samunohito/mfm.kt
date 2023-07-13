package com.github.samunohito.mfm.node

data class MfmSmall(override val children: List<IMfmInline<*>>) : IMfmInline<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Small
  override val props = MfmPropsEmpty
}