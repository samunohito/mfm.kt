package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmBold

class BoldTagParser : IParser<MfmBold> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmBold> {
    return ParserResult.ofFailure()
  }
}