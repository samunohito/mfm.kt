package com.github.samunohito.mfm.internal.core2.parser.core

import com.github.samunohito.mfm.internal.core2.parser.IParser2
import com.github.samunohito.mfm.internal.core2.parser.Result2

class RegexParser(private val pattern: Regex) : IParser2<String> {
  override fun parse(input: String, startAt: Int): Result2<String> {
    val text = input.slice(startAt..input.length)
    val result = pattern.find(text)
    return if (result == null) {
      Result2.ofFailure()
    } else {
      Result2.ofSuccess(startAt..result.value.length, result.value)
    }
  }
}