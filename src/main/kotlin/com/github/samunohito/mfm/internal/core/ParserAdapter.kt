package com.github.samunohito.mfm.internal.core

import com.github.samunohito.mfm.IParser
import com.github.samunohito.mfm.node.IMfmNode

class ParserAdapter<T : IMfmNode<*>>(private val parser: IParser<T>) : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val result = parser.parse(input, startAt)
    if (!result.success) {
      return Result.ofFailure<T>()
    }

    return Result.ofSuccess(result.range, result.next, listOf(), result.node)
  }

  class Result<T : IMfmNode<*>> private constructor(
    success: Boolean,
    range: IntRange,
    next: Int,
    subResults: List<SubstringFinderResult>,
    node: T?,
  ) : SubstringFinderResult(success, range, next, subResults) {
    private val _node: T? = node
    val node: T
      get() = if (success) {
        _node!!
      } else {
        error("node is null")
      }

    companion object {
      fun <T : IMfmNode<*>> ofSuccess(
        range: IntRange,
        next: Int,
        nestResult: List<SubstringFinderResult> = listOf(),
        node: T,
      ): Result<T> {
        return Result(true, range, next, nestResult, node)
      }

      fun <T : IMfmNode<*>> ofFailure(): Result<T> {
        return Result(false, IntRange.EMPTY, -1, listOf(), null)
      }
    }
  }
}