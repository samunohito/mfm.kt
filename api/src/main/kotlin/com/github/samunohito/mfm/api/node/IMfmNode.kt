package com.github.samunohito.mfm.api.node

interface IMfmNode {
  val type: MfmNodeType
  fun stringify(): String
}