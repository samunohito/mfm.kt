package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmBold
import com.github.samunohito.mfm.node.MfmNodeAttribute
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class BoldTagNodeFactory : SimpleNodeFactoryBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldTag)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmBold> {
    val result = NodeFactoryUtils.createNodes(input, foundInfo.sub, MfmNodeAttribute.setOfInline)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmBold(result), foundInfo)
  }
}