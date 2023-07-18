package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmCenter

class CenterTagParser : SimpleParserBase<MfmCenter>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.CenterTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmCenter> {
    return failure()
  }
}