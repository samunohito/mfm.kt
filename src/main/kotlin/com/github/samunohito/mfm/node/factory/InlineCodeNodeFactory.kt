package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmInlineCode

class InlineCodeNodeFactory : SimpleNodeFactoryBase<MfmInlineCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.InlineCode)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmInlineCode> {
    return success(MfmInlineCode(input.substring(foundInfo.range)), foundInfo)
  }
}