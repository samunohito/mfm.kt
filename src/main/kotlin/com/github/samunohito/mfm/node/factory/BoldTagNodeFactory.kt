package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmBold
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class BoldTagNodeFactory : SimpleNodeFactoryBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldTag)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmBold> {
    val result = NodeFactoryUtils.recursiveInline(input, foundInfo)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmBold(result), foundInfo)
  }
}