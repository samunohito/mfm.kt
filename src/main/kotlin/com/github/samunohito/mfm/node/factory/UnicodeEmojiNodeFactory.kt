package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmUnicodeEmoji

class UnicodeEmojiNodeFactory : SimpleNodeFactoryBase<MfmUnicodeEmoji>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.UnicodeEmoji)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmUnicodeEmoji> {
    return success(MfmUnicodeEmoji(input.substring(foundInfo.range)), foundInfo)
  }
}