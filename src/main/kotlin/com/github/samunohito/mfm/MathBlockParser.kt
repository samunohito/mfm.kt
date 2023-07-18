package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.MathBlockFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmMathBlock

class MathBlockParser : SimpleParserBase<MfmMathBlock>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.MathBlock)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmMathBlock> {
    return success(MfmMathBlock(input.substring(foundInfo.range)), foundInfo)
  }
}