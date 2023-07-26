package com.github.samunohito.mfm.api.node

class MfmMention(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmMention.Props> {
  constructor(username: String, host: String?) : this(Props(username, host))

  override val type = MfmNodeType.Mention

  override fun stringify(): String {
    return props.acct
  }

  class Props(username: String, host: String?) : IMfmProps {
    var username: String = username
      set(value) {
        field = value
        acct = buildAcct()
      }

    var host: String? = host
      set(value) {
        field = value
        acct = buildAcct()
      }

    // acctは自動で作るようにしたいが、getterにするとシリアライズから除外されてしまうのでプロパティにしている
    var acct: String = buildAcct()
      private set

    private fun buildAcct(): String {
      return if (host != null) {
        "@$username@$host"
      } else {
        "@$username"
      }
    }

    override fun toString(): String {
      return "Props(username='$username', host=$host, acct='$acct')"
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (javaClass != other?.javaClass) return false

      other as Props

      if (username != other.username) return false
      if (host != other.host) return false
      return acct == other.acct
    }

    override fun hashCode(): Int {
      var result = username.hashCode()
      result = 31 * result + (host?.hashCode() ?: 0)
      result = 31 * result + acct.hashCode()
      return result
    }
  }
}