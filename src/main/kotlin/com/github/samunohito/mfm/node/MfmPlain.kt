package com.github.samunohito.mfm.node

data class MfmPlain(override val children: List<MfmText>) : IMfmBlock<MfmPropsEmpty>,
  IMfmIncludeChildren {
  override val type = MfmNodeType.Plain
  override val props = MfmPropsEmpty
}