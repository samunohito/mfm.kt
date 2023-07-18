package com.github.samunohito.mfm.node

interface IMfmNodePropertyHolder<T : IMfmProps> {
  val props: T
}