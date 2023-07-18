package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmStrike
import com.github.samunohito.mfm.node.MfmText

class StrikeTagParser : SimpleParserBase<MfmStrike>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.StrikeTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmStrike> {
    return success(MfmStrike(MfmText(input.substring(foundInfo.range))), foundInfo)
  }
}