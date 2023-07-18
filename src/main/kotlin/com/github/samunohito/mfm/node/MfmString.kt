package com.github.samunohito.mfm.node

class MfmString(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmString.Props> {
  override val type = MfmNodeType.String

  constructor(value: String) : this(Props(value))

  override fun toString(): String {
    return props.value
  }

  data class Props(val value: String) : IMfmProps
}