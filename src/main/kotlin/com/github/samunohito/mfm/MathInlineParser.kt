package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.MathInlineFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmMathInline

class MathInlineParser : SimpleParserBase<MfmMathInline, MathInlineFinder>() {
  override val finder = MathInlineFinder()
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.MathInline)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmMathInline> {
    return success(MfmMathInline(input.substring(foundInfo.range)), foundInfo)
  }
}