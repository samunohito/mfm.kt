package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmSmall

class SmallTagParser : IParser<MfmSmall> {
  override fun parse(input: String, startAt: Int): IParserResult<MfmSmall> {
    return failure()
  }
}