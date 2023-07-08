package com.github.samunohito.mfm.internal.core2.parser.core.singleton

import com.github.samunohito.mfm.internal.core2.parser.IParser2
import com.github.samunohito.mfm.internal.core2.parser.Result2

object CharParser : IParser2<String> {
  override fun parse(input: String, startAt: Int): Result2<String> {
    if (input.isEmpty()) {
      return Result2.ofFailure()
    }

    val char = input[startAt]
    return Result2.ofSuccess(startAt..(startAt + 1), char.toString())
  }
}