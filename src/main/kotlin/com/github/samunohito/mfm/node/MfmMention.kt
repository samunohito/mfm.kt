package com.github.samunohito.mfm.node

class MfmMention(override val props: Props) : IMfmNode, IMfmNodePropertyHolder<MfmMention.Props> {
  override val type = MfmNodeType.Mention

  constructor(username: String, host: String?, acct: String) : this(Props(username, host, acct))

  class Props(val username: String, val host: String?, val acct: String) : IMfmProps
}