package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmBold

class BoldTagParser : SimpleParserBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmBold> {
    return failure()
  }
}