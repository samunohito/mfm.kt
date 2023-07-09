package com.github.samunohito.mfm.node

data class MfmLink(
  override val props: Props,
  override val children: List<IMfmInline<*>>
) : IMfmBlock<MfmLink.Props>,
  IMfmIncludeChildren {
  override val type = MfmNodeType.Link

  data class Props(val silent: Boolean, val url: String) : IMfmProps
}