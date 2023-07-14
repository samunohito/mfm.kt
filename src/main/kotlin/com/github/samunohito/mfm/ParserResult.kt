package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.IMfmNode

class ParserResult<T : IMfmNode<*>>(
  val success: Boolean,
  node: T?,
  val range: IntRange,
  val next: Int,
) {
  private val _node: T? = node

  val node: T
    get() = if (success) {
      _node!!
    } else {
      error("node is null")
    }

  companion object {
    fun <T : IMfmNode<*>> ofSuccess(node: T, range: IntRange, next: Int): ParserResult<T> {
      return ParserResult(true, node, range, next)
    }

    fun <T : IMfmNode<*>> ofFailure(): ParserResult<T> {
      return ParserResult(false, null, IntRange.EMPTY, 0)
    }
  }
}