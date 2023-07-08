package com.github.samunohito.mfm.internal.core2.parser.core.singleton

import com.github.samunohito.mfm.internal.core2.parser.IParser2
import com.github.samunohito.mfm.internal.core2.parser.Result2

object LineBeginParser : IParser2<String> {
  override fun parse(input: String, startAt: Int): Result2<String> {
    val result = when {
      input.length == startAt -> true
      CrParser.parse(input, startAt - 1).success -> true
      LfParser.parse(input, startAt - 1).success -> true
      else -> false
    }

    return if (result) {
      Result2.ofSuccess(startAt..startAt, "")
    } else {
      Result2.ofFailure()
    }
  }
}