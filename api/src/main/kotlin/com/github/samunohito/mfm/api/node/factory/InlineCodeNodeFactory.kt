package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmInlineCode

class InlineCodeNodeFactory : SimpleNodeFactoryBase<MfmInlineCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.InlineCode)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmInlineCode> {
    return success(MfmInlineCode(input.substring(foundInfo.contentRange)), foundInfo)
  }
}