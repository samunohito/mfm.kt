package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.MfmSmall
import com.github.samunohito.mfm.api.node.factory.NodeFactory
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object SmallTagNodeFactory : SimpleNodeFactoryBase<MfmSmall>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.SmallTag)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmSmall> {
    val result = NodeFactory.createNodes(input, foundInfo.nestedInfos, context, MfmNodeAttribute.setOfInline)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmSmall(result), foundInfo)
  }
}