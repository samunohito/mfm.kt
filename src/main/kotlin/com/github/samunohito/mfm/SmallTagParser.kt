package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmSmall
import com.github.samunohito.mfm.node.MfmText

class SmallTagParser : SimpleParserBase<MfmSmall>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.SmallTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmSmall> {
    return success(MfmSmall(MfmText(input.substring(foundInfo.range))), foundInfo)
  }
}