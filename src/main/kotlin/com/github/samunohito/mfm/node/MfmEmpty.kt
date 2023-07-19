package com.github.samunohito.mfm.node

object MfmEmpty : IMfmNode, IMfmInline, IMfmBlock {
  override val type = MfmNodeType.Empty
}