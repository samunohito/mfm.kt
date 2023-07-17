package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmFn

class FnParser : IParser<MfmFn> {
  override fun parse(input: String, startAt: Int): IParserResult<MfmFn> {
    return failure()
  }
}