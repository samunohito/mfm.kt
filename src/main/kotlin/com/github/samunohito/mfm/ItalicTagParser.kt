package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmItalic

class ItalicTagParser : SimpleParserBase<MfmItalic>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.ItalicTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmItalic> {
    return failure()
  }
}