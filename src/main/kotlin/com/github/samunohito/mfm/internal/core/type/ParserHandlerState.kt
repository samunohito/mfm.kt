package com.github.samunohito.mfm.internal.core.type

data class ParserHandlerState(
  val nestLimit: Int?,
  var depth: Int,
  val linkLabel: Boolean,
  val trace: Boolean,
)
