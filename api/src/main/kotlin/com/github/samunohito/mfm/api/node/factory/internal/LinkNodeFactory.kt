package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.inline.LinkFinder
import com.github.samunohito.mfm.api.node.MfmLink
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.NodeFactory

object LinkNodeFactory : SimpleNodeFactoryBase<MfmLink>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Link)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmLink> {
    val squareOpen = foundInfo[LinkFinder.SubIndex.SquareOpen]
    val label = foundInfo[LinkFinder.SubIndex.Label]
    val url = foundInfo[LinkFinder.SubIndex.Url]

    val isSilent = (input.substring(squareOpen.contentRange) == "?[")
    val urlText = input.substring(url.contentRange)

    val labelContents = NodeFactory.createNodes(
      input,
      label.nestedInfos,
      context,
      MfmNodeAttribute.setOfInline
    )
    if (labelContents.isEmpty()) {
      return failure()
    }

    val node = MfmLink(isSilent, urlText, labelContents)
    return success(node, foundInfo)
  }
}