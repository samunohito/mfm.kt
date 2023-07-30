package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.FullFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.MfmQuote
import com.github.samunohito.mfm.api.node.factory.NodeFactory

object QuoteNodeFactory : SimpleNodeFactoryBase<MfmQuote>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Quote)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmQuote> {
    val contentLines = foundInfo.nestedInfos.map { input.substring(it.contentRange) }
    if (contentLines.isEmpty() || (contentLines.size == 1 && contentLines[0].isEmpty())) {
      // disallow empty content if single line
      return failure()
    }

    // parse inner content
    val finderContext = ISubstringFinderContext.Impl()
    val contentText = contentLines.joinToString("\n")
    val contentFindResult = FullFinder().find(contentText, 0, finderContext)
    if (!contentFindResult.success) {
      return failure()
    }

    val result = NodeFactory.createNodes(
      contentText,
      contentFindResult.foundInfo.nestedInfos,
      context,
      MfmNodeAttribute.setOfAll
    )
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmQuote(result), foundInfo)
  }
}