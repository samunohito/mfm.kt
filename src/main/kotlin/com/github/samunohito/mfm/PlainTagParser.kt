package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmPlain
import com.github.samunohito.mfm.node.MfmText

class PlainTagParser : SimpleParserBase<MfmPlain>() {
  override val supportFoundTypes = setOf(FoundType.PlainTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmPlain> {
    val textNode = MfmText(input.substring(foundInfo.range))
    return success(MfmPlain(listOf(textNode)), foundInfo)
  }
}