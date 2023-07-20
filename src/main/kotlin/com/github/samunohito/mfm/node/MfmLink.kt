package com.github.samunohito.mfm.node

class MfmLink(
  override val props: Props,
  children: List<IMfmNode>
) : IMfmNode, IMfmNodePropertyHolder<MfmLink.Props>, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Link

  constructor(silent: Boolean, url: String, children: List<IMfmNode>) : this(Props(silent, url), children)

  class Props(val silent: Boolean, val url: String) : IMfmProps
}