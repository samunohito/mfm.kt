package com.github.samunohito.mfm.node

class MfmMention(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmMention.Props> {
  constructor(username: String, host: String?) : this(Props(username, host))

  override val type = MfmNodeType.Mention

  override fun stringify(): String {
    return props.acct
  }

  data class Props(var username: String, var host: String?) : IMfmProps {
    val acct: String
      get() = if (host == null) {
        "@$username"
      } else {
        "@$username@$host"
      }
  }
}