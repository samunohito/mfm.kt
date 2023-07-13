package com.github.samunohito.mfm.node

data class MfmLink(
  override val props: Props,
  override val children: List<IMfmInline<*>>
) : IMfmInline<MfmLink.Props>, IMfmIncludeChildren {
  override val type = MfmNodeType.Link

  data class Props(val silent: Boolean, val url: String) : IMfmProps
}