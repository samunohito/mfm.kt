package com.github.samunohito.mfm.internal.core.type.node

class MfmPlain(override val children: List<MfmText>) : IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.HashTag
  override val props = MfmPropsEmpty
}