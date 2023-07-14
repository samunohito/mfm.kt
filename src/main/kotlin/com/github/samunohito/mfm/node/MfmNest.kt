package com.github.samunohito.mfm.node

class MfmNest<T : IMfmNode<*>>(
  override val children: List<T>
) : IMfmInline<MfmPropsEmpty>, IMfmBlock<MfmPropsEmpty>, IMfmIncludeChildren {
  override val type = MfmNodeType.Nest
  override val props = MfmPropsEmpty
}