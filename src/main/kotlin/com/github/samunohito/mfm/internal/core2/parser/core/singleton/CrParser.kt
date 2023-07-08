package com.github.samunohito.mfm.internal.core2.parser.core.singleton

import com.github.samunohito.mfm.internal.core2.parser.IParser2
import com.github.samunohito.mfm.internal.core2.parser.Result2
import com.github.samunohito.mfm.internal.core2.parser.core.RegexParser

object CrParser : IParser2<String> {
  private val parser = RegexParser(Regex("\r"))

  override fun parse(input: String, startAt: Int): Result2<String> {
    return parser.parse(input, startAt)
  }
}