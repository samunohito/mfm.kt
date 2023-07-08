package com.github.samunohito.mfm.internal.core2.parser.core

import com.github.samunohito.mfm.internal.core2.parser.IParser2
import com.github.samunohito.mfm.internal.core2.parser.Result2

class StringParser(private val word: String) : IParser2<String> {
  override fun parse(input: String, startAt: Int): Result2<String> {
    return if ((input.length - startAt) < word.length) {
      Result2.ofFailure()
    } else if (input.substring(startAt, word.length) != word) {
      Result2.ofFailure()
    } else {
      Result2.ofSuccess(startAt..(startAt + word.length), word)
    }
  }
}