package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmMathInline

class MathInlineNodeFactory : SimpleNodeFactoryBase<MfmMathInline>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.MathInline)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmMathInline> {
    return success(MfmMathInline(input.substring(foundInfo.range)), foundInfo)
  }
}