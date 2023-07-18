package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmStrike

class StrikeTagNodeFactory : SimpleNodeFactoryBase<MfmStrike>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.StrikeTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmStrike> {
    val result = InlineNodeFactory().parse(input, foundInfo)
    if (!result.success) {
      return failure()
    }

    return success(MfmStrike(result.node.children.filterIsInstance(IMfmInline::class.java)), foundInfo)
  }
}