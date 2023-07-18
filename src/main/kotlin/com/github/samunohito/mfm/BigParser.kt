package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmFn

class BigParser : SimpleParserBase<MfmFn>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Fn)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmFn> {
    return failure()
  }
}