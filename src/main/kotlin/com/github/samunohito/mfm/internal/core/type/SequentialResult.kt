package com.github.samunohito.mfm.internal.core.type

import com.github.samunohito.mfm.internal.core.Parser

class SequentialResult<T>(
  private val parser: Parser<List<T>>
) {
  fun all(): Parser<List<T>> {
    return parser
  }

  fun select(index: Int): Parser<T> {
    return parser.map { it[index] }
  }
}