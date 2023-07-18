package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmCenter

class CenterTagParser : SimpleParserBase<MfmCenter>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.CenterTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmCenter> {
    val result = InlineParser().parse(input, foundInfo)
    if (!result.success) {
      return failure()
    }

    return success(MfmCenter(result.node.children.filterIsInstance(IMfmInline::class.java)), foundInfo)
  }
}