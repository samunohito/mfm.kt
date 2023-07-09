package com.github.samunohito.mfm.node

object MfmEmpty : IMfmNode<MfmPropsEmpty> {
  override val type = MfmNodeType.Empty
  override val props = MfmPropsEmpty
}