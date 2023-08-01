package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmItalic
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.NodeFactory
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object ItalicTagNodeFactory : SimpleNodeFactoryBase<MfmItalic>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.ItalicTag)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmItalic> {
    val result = NodeFactory.createNodes(input, foundInfo.nestedInfos, context, MfmNodeAttribute.setOfInline)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmItalic(result), foundInfo)
  }
}