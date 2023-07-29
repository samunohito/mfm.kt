package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmUnicodeEmoji

object UnicodeEmojiNodeFactory : SimpleNodeFactoryBase<MfmUnicodeEmoji>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.UnicodeEmoji)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmUnicodeEmoji> {
    return success(MfmUnicodeEmoji(input.substring(foundInfo.contentRange)), foundInfo)
  }
}