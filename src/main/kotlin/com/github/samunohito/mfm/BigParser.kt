package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmFn

class BigParser : IParser<MfmFn> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmFn> {
    return ParserResult.ofFailure()
  }
}