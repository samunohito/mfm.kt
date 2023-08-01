package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmUnicodeEmoji
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

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