package com.github.samunohito.mfm.node

interface IMfmNode {
  val type: MfmNodeType
  fun stringify(): String
}