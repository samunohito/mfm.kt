package com.github.samunohito.mfm.api.node

import com.github.samunohito.mfm.api.MfmUtils

class MfmFn(
  override val props: Props,
  children: List<IMfmNode>
) : MfmNodeChildrenHolderBase(children), IMfmNodePropertyHolder<MfmFn.Props> {
  constructor(name: String, args: Map<String, Any>, children: List<IMfmNode>) : this(Props(name, args), children)
  constructor(name: String, args: Map<String, Any>, vararg children: IMfmNode) : this(
    Props(name, args),
    children.toList()
  )

  override val type = MfmNodeType.Fn
  override fun stringify(): String {
    val argFields = props.args.map { (key, value) ->
      if (value == Unit) {
        key
      } else {
        "$key=$value"
      }
    }
    val args = if (argFields.isNotEmpty()) {
      "." + argFields.joinToString(",")
    } else {
      ""
    }

    return "$[${props.name}${args} ${MfmUtils.stringify(children)}]"
  }

  data class Props(var name: String, var args: Map<String, Any>) : IMfmProps
}