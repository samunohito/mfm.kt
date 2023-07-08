package com.github.samunohito.mfm.internal.core2.parser.core.singleton

import com.github.samunohito.mfm.internal.core2.parser.IParser2
import com.github.samunohito.mfm.internal.core2.parser.ParserUtils
import com.github.samunohito.mfm.internal.core2.parser.Result2

object NewLineParser : IParser2<String> {
  override fun parse(input: String, startAt: Int): Result2<String> {
    return ParserUtils.alternative(input, startAt, CrParser, LfParser, CrLfParser)
  }
}