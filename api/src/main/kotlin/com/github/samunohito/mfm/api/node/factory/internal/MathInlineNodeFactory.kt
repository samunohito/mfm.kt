package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmMathInline

object MathInlineNodeFactory : SimpleNodeFactoryBase<MfmMathInline>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.MathInline)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmMathInline> {
    return success(MfmMathInline(input.substring(foundInfo.contentRange)), foundInfo)
  }
}