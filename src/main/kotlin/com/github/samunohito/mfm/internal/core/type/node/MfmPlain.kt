package com.github.samunohito.mfm.internal.core.type.node

data class MfmPlain(override val children: List<MfmText>) : IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Plain
  override val props = MfmPropsEmpty
}