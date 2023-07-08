package com.github.samunohito.mfm.internal.core.type.node

interface IMfmNode<T : IMfmProps> {
  val type: MfmNodeType
  val props: T
}