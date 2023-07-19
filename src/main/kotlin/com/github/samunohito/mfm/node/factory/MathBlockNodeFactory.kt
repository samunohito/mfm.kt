package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmMathBlock

class MathBlockNodeFactory : SimpleNodeFactoryBase<MfmMathBlock>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.MathBlock)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmMathBlock> {
    return success(MfmMathBlock(input.substring(foundInfo.range)), foundInfo)
  }
}