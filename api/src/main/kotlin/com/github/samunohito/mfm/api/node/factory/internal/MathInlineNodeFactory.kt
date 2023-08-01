package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmMathInline
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

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