package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmCenter
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.NodeFactory
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object CenterTagNodeFactory : SimpleNodeFactoryBase<MfmCenter>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.CenterTag)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmCenter> {
    val result = NodeFactory.createNodes(input, foundInfo.nestedInfos, context, MfmNodeAttribute.setOfInline)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmCenter(result), foundInfo)
  }
}