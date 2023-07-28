package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmFn
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.utils.NodeFactoryUtils

class BigNodeFactory : SimpleNodeFactoryBase<MfmFn>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Big)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmFn> {
    val result = NodeFactoryUtils.createNodes(input, foundInfo.sub, MfmNodeAttribute.setOfInline, context)
    if (result.isEmpty()) {
      return failure()
    }

    val node = MfmFn("tada", emptyMap(), result)
    return success(node, foundInfo)
  }
}