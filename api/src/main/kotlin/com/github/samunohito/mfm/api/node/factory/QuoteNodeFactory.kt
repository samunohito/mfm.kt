package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.FullFinder
import com.github.samunohito.mfm.api.finder.IRecursiveFinderContext
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmQuote
import com.github.samunohito.mfm.api.node.factory.utils.NodeFactoryUtils

class QuoteNodeFactory : SimpleNodeFactoryBase<MfmQuote>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Quote)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmQuote> {
    val contentLines = foundInfo.sub.map { input.substring(it.range) }

    if (contentLines.isEmpty() || (contentLines.size == 1 && contentLines[0].isEmpty())) {
      // disallow empty content if single line
      return failure()
    }

    // parse inner content
    val contentText = contentLines.joinToString("\n")
    val contentFindResult = FullFinder(context = IRecursiveFinderContext.Impl()).find(contentText)
    if (!contentFindResult.success) {
      return failure()
    }

    val result = NodeFactoryUtils.createNodes(contentText, contentFindResult.foundInfo.sub)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmQuote(result), foundInfo)
  }
}