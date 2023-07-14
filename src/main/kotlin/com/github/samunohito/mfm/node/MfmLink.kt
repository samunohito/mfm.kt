package com.github.samunohito.mfm.node

data class MfmLink(
  override val props: Props,
  override val children: List<IMfmInline<*>>
) : IMfmInline<MfmLink.Props>, IMfmIncludeChildren {
  override val type = MfmNodeType.Link

  constructor(silent: Boolean, url: String, children: List<IMfmInline<*>>) : this(Props(silent, url), children)

  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromNest(silent: Boolean, url: String, nest: MfmNest<*>): MfmLink {
      // Linkの子要素はInline系しか受け付けない
      nest.children.forEach { require(it is IMfmInline) }

      return MfmLink(silent, url, nest.children as List<IMfmInline<*>>)
    }
  }

  data class Props(val silent: Boolean, val url: String) : IMfmProps
}