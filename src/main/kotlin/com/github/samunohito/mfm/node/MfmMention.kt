package com.github.samunohito.mfm.node

data class MfmMention(override val props: Props) : IMfmInline<MfmMention.Props> {
  override val type = MfmNodeType.Mention

  constructor(username: String, host: String?, acct: String) : this(Props(username, host, acct))


  data class Props(val username: String, val host: String?, val acct: String) :
    IMfmProps
}