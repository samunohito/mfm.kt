package com.github.samunohito.mfm.internal.core.type.node

data class MfmStrike(override val children: List<IMfmInline<*>>) : IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Strike
  override val props = MfmPropsEmpty
}