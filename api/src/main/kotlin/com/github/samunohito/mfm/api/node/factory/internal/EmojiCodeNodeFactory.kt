package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmEmojiCode
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object EmojiCodeNodeFactory : SimpleNodeFactoryBase<MfmEmojiCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.EmojiCode)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmEmojiCode> {
    return success(MfmEmojiCode(input.substring(foundInfo.contentRange)), foundInfo)
  }
}