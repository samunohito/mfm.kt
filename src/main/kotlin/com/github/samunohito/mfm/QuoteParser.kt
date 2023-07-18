package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmQuote

class QuoteParser : SimpleParserBase<MfmQuote>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Quote)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmQuote> {
    return failure()
  }
}