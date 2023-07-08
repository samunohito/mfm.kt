package com.github.samunohito.mfm.internal.core.type

data class Params(
  val input: String,
  val index: Int,
  val state: ParserHandlerState,
) {
  fun getCharAtIndex(): Char {
    if (index < 0 || index >= input.length) {
      error("empty")
    }
    return input[index]
  }
}
