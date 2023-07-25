package com.github.samunohito.mfm.api.node

interface IMfmNodePropertyHolder<T : IMfmProps> {
  val props: T
}