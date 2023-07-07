package com.github.samunohito.mfm.internal.core.type.node

interface MfmNode<T : MfmProps> {
  val type: MfmNodeType
  val props: T
}