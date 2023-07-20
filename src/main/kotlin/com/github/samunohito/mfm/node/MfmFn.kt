package com.github.samunohito.mfm.node

class MfmFn(
  override val props: Props,
  children: List<IMfmNode>
) : IMfmNode, IMfmNodePropertyHolder<MfmFn.Props>, MfmNodeNestableBase(children) {
  override val type = MfmNodeType.Fn

  constructor(name: String, args: Map<String, Any>, children: List<IMfmNode>) : this(Props(name, args), children)

  class Props(val name: String, val args: Map<String, Any>) : IMfmProps
}