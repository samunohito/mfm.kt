package com.github.samunohito.mfm.internal.core.type.node

data class MfmFn(
  override val props: Props,
  override val children: List<IMfmInline<*>>
) : IMfmBlock<MfmFn.Props>, IMfmIncludeChildren {
  override val type = MfmNodeType.Fn

  data class Props(val name: Boolean, val args: Map<String, Any>) : IMfmProps
}