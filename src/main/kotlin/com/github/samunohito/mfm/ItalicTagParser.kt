package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmItalic

class ItalicTagParser : IParser<MfmItalic> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmItalic> {
    return ParserResult.ofFailure()
  }
}