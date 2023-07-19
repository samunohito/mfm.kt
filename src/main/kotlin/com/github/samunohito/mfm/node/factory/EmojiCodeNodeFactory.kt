package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmEmojiCode

class EmojiCodeNodeFactory : SimpleNodeFactoryBase<MfmEmojiCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.EmojiCode)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmEmojiCode> {
    return success(MfmEmojiCode(input.substring(foundInfo.range)), foundInfo)
  }
}