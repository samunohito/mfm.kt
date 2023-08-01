package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmLink
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.NodeFactory
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.inline.LinkParser

object LinkNodeFactory : SimpleNodeFactoryBase<MfmLink>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Link)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmLink> {
    val squareOpen = foundInfo[LinkParser.SubIndex.SquareOpen]
    val label = foundInfo[LinkParser.SubIndex.Label]
    val url = foundInfo[LinkParser.SubIndex.Url]

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