package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmStrike

class StrikeWaveParser : IParser<MfmStrike> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmStrike> {
    return ParserResult.ofFailure()
  }
}