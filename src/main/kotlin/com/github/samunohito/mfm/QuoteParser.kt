package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmQuote

class QuoteParser : IParser<MfmQuote> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmQuote> {
    return ParserResult.ofFailure()
  }
}