package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmUnicodeEmoji

class UnicodeEmojiNodeFactory : SimpleNodeFactoryBase<MfmUnicodeEmoji>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.UnicodeEmoji)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmUnicodeEmoji> {
    return success(MfmUnicodeEmoji(input.substring(foundInfo.range)), foundInfo)
  }
}