package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmMathBlock
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object MathBlockNodeFactory : SimpleNodeFactoryBase<MfmMathBlock>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.MathBlock)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmMathBlock> {
    return success(MfmMathBlock(input.substring(foundInfo.contentRange)), foundInfo)
  }
}