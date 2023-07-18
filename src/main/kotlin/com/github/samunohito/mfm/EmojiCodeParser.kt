package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmEmojiCode

class EmojiCodeParser : SimpleParserBase<MfmEmojiCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.EmojiCode)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmEmojiCode> {
    return success(MfmEmojiCode(input.substring(foundInfo.range)), foundInfo)
  }
}