package com.github.samunohito.mfm.internal.core.type.node

data class MfmMention(override val props: Props) : IMfmBlock<MfmMention.Props> {
  override val type = MfmNodeType.Mention

  data class Props(val username: String, val host: String?, val acct: String) : IMfmProps
}