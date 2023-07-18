package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmFn

class BigParser : SimpleParserBase<MfmFn>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Fn)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmFn> {
    val result = InlineParser().parse(input, foundInfo)
    if (!result.success) {
      return failure()
    }

    val node = MfmFn("tada", emptyMap(), result.node.children.filterIsInstance(IMfmInline::class.java))
    return success(node, foundInfo)
  }
}