package com.github.samunohito.mfm.node

class MfmFn(
  override val props: Props,
  children: List<IMfmNode>
) : MfmNodeChildrenHolderBase(children), IMfmNodePropertyHolder<MfmFn.Props> {
  override val type = MfmNodeType.Fn

  constructor(name: String, args: Map<String, Any>, children: List<IMfmNode>) : this(Props(name, args), children)

  data class Props(val name: String, val args: Map<String, Any>) : IMfmProps
}