package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmBold

class BoldAstaParser : IParser<MfmBold> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmBold> {
    return ParserResult.ofFailure()
  }
}