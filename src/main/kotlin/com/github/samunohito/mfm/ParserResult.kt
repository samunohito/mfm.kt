package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.IMfmNode

class ParserResult<T : IMfmNode<*>>(
  val success: Boolean,
  val node: T,
  val input: String,
  val range: IntRange,
  val next: Int,
) {
  companion object {
    fun <T : IMfmNode<*>> ofSuccess(node: T, input: String, range: IntRange, next: Int): ParserResult<T> {
      return ParserResult(true, node, input, range, next)
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    fun <T : IMfmNode<*>> ofFailure(): ParserResult<T> {
      return ParserResult(false, null as T, "", IntRange.EMPTY, 0)
    }
  }
}