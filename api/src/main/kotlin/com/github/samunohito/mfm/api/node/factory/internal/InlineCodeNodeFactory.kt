package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmInlineCode
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object InlineCodeNodeFactory : SimpleNodeFactoryBase<MfmInlineCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.InlineCode)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmInlineCode> {
    return success(MfmInlineCode(input.substring(foundInfo.contentRange)), foundInfo)
  }
}