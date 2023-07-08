package com.github.samunohito.mfm.internal.core.type.node

class MfmString(override val props: Props) : IMfmNode<MfmString.Props> {
  override val type = MfmNodeType.String

  constructor(value: String) : this(Props(value))

  override fun toString(): String {
    return props.value
  }

  data class Props(val value: String) : IMfmProps
}