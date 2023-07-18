package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.UnicodeEmojiFinder
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmUnicodeEmoji

class UnicodeEmojiParser : SimpleParserBase<MfmUnicodeEmoji>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.UnicodeEmoji)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmUnicodeEmoji> {
    // TODO: Implement
    return failure()
  }
}