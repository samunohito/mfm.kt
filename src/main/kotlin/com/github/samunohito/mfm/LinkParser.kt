package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmLink

class LinkParser : IParser<MfmLink> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmLink> {
    return ParserResult.ofFailure()
  }
}