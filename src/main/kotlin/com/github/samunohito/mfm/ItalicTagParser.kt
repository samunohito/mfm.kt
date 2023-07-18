package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmItalic

class ItalicTagParser : SimpleParserBase<MfmItalic>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.ItalicTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmItalic> {
    val result = InlineParser().parse(input, foundInfo)
    if (!result.success) {
      return failure()
    }

    return success(MfmItalic(result.node.children.filterIsInstance(IMfmInline::class.java)), foundInfo)
  }
}