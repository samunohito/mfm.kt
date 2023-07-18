package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmInlineCode

class InlineCodeParser : SimpleParserBase<MfmInlineCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.InlineCode)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmInlineCode> {
    return success(MfmInlineCode(input.substring(foundInfo.sub[1].range)), foundInfo)
  }
}