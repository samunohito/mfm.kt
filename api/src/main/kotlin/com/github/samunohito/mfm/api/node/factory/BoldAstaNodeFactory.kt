package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmBold
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.utils.NodeFactoryUtils

class BoldAstaNodeFactory : SimpleNodeFactoryBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldAsta)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmBold> {
    val result = NodeFactoryUtils.createNodes(input, foundInfo.sub, MfmNodeAttribute.setOfInline)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmBold(result), foundInfo)
  }
}