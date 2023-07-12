package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmUnicodeEmoji

class UnicodeEmojiParser : IParser<MfmUnicodeEmoji> {
  override fun parse(input: String, startAt: Int): ParserResult<MfmUnicodeEmoji> {
    return ParserResult.ofFailure()
  }
}