package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmItalic

class ItalicTagNodeFactory : SimpleNodeFactoryBase<MfmItalic>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.ItalicTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmItalic> {
    val result = InlineNodeFactory().parse(input, foundInfo)
    if (!result.success) {
      return failure()
    }

    return success(MfmItalic(result.node.children.filterIsInstance(IMfmInline::class.java)), foundInfo)
  }
}