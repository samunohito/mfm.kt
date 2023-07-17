package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmQuote

class QuoteParser : IParser<MfmQuote> {
  override fun parse(input: String, startAt: Int): IParserResult<MfmQuote> {
    return failure()
  }
}