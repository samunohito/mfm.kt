package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmSmall

class SmallTagNodeFactory : SimpleNodeFactoryBase<MfmSmall>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.SmallTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmSmall> {
    val result = InlineNodeFactory().parse(input, foundInfo)
    if (!result.success) {
      return failure()
    }

    return success(MfmSmall(result.node.children.filterIsInstance(IMfmInline::class.java)), foundInfo)
  }
}