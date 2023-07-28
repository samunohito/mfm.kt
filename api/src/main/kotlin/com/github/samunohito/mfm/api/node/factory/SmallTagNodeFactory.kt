package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.MfmSmall
import com.github.samunohito.mfm.api.node.factory.utils.NodeFactoryUtils

class SmallTagNodeFactory : SimpleNodeFactoryBase<MfmSmall>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.SmallTag)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmSmall> {
    val result = NodeFactoryUtils.createNodes(input, foundInfo.sub, MfmNodeAttribute.setOfInline, context)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmSmall(result), foundInfo)
  }
}