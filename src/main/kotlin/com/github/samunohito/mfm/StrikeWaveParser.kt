package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmStrike
import com.github.samunohito.mfm.node.MfmText

class StrikeWaveParser : SimpleParserBase<MfmStrike>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.StrikeWave)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmStrike> {
    return success(MfmStrike(MfmText(input.substring(foundInfo.range))), foundInfo)
  }
}