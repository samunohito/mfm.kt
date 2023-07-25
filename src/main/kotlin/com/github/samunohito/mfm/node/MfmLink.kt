package com.github.samunohito.mfm.node

import com.github.samunohito.mfm.MfmUtils

class MfmLink(
  override val props: Props,
  children: List<IMfmNode>
) : MfmNodeChildrenHolderBase(children), IMfmNode, IMfmNodePropertyHolder<MfmLink.Props> {
  constructor(silent: Boolean, url: String, children: List<IMfmNode>) : this(Props(silent, url), children)

  constructor(silent: Boolean, url: String, vararg children: IMfmNode) : this(silent, url, children.toList())

  override val type = MfmNodeType.Link

  override fun stringify(): String {
    val prefix = if (props.silent) {
      "?"
    } else {
      ""
    }

    return "${prefix}[${MfmUtils.stringify(children)}](${props.url})"
  }

  data class Props(var silent: Boolean, var url: String) : IMfmProps
}