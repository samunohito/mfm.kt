package com.github.samunohito.mfm.node

class MfmUrl(override val props: Props) : MfmNodeBase(), IMfmNode, IMfmNodePropertyHolder<MfmUrl.Props> {
  override val type = MfmNodeType.Url

  constructor(url: String, brackets: Boolean?) : this(Props(url, brackets))

  override fun stringify(): String {
    return if (props.brackets == true) {
      "<${props.url}>"
    } else {
      props.url
    }
  }

  data class Props(val url: String, val brackets: Boolean?) : IMfmProps
}