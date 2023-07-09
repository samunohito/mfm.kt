package com.github.samunohito.mfm.node

interface IMfmNode<T : IMfmProps> {
  val type: MfmNodeType
  val props: T
}