package com.github.samunohito.mfm.node

data class MfmCenter(override val children: List<IMfmInline<*>>) :
  IMfmBlock<MfmPropsEmpty>,
  IMfmIncludeChildren {
  override val type = MfmNodeType.Center
  override val props = MfmPropsEmpty
}