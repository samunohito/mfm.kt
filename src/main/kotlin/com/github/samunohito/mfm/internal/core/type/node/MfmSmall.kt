package com.github.samunohito.mfm.internal.core.type.node

data class MfmSmall(override val children: List<IMfmInline<*>>) : IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Small
  override val props = MfmPropsEmpty
}