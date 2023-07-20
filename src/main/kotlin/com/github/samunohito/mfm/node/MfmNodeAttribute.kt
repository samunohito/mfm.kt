package com.github.samunohito.mfm.node

enum class MfmNodeAttribute {
  Block,
  Inline,
  Simple,
  Virtuality,
  ;

  companion object {
    val setOfAll = setOf(
      Block,
      Inline,
      Simple,
      Virtuality,
    )
    val setOfInline = setOf(
      Inline,
      Virtuality,
    )
    val setOfSimple = setOf(
      Simple,
    )
  }
}