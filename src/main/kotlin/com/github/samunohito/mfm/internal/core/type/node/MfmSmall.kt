package com.github.samunohito.mfm.internal.core.type.node

class MfmBold(override val children: List<IMfmInline<*>>) : IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Bold
  override val props = MfmPropsEmpty
}