package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmBold

class BoldAstaNodeFactory : SimpleNodeFactoryBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldAsta)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmBold> {
    val result = InlineNodeFactory().parse(input, foundInfo)
    if (!result.success) {
      return failure()
    }

    return success(MfmBold(result.node.children.filterIsInstance(IMfmInline::class.java)), foundInfo)
  }
}