package com.github.samunohito.mfm.node

class MfmLink(
  override val props: Props,
  children: List<IMfmNode>
) : MfmNodeChildrenHolderBase(children), IMfmNode, IMfmNodePropertyHolder<MfmLink.Props> {
  override val type = MfmNodeType.Link

  constructor(silent: Boolean, url: String, children: List<IMfmNode>) : this(Props(silent, url), children)
  constructor(silent: Boolean, url: String, vararg children: IMfmNode) : this(silent, url, children.toList())

  data class Props(val silent: Boolean, val url: String) : IMfmProps
}