package com.github.samunohito.mfm.node

class MfmMention(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmMention.Props> {
  constructor(username: String, host: String?, acct: String) : this(Props(username, host, acct))

  override val type = MfmNodeType.Mention

  override fun stringify(): String {
    return props.acct
  }

  data class Props(val username: String, val host: String?, val acct: String) : IMfmProps
}