package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmMathBlock

class MathBlockNodeFactory : SimpleNodeFactoryBase<MfmMathBlock>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.MathBlock)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmMathBlock> {
    return success(MfmMathBlock(input.substring(foundInfo.contentRange)), foundInfo)
  }
}