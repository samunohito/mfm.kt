package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.LinkFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmLink
import com.github.samunohito.mfm.node.MfmNodeAttribute
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class LinkNodeFactory : SimpleNodeFactoryBase<MfmLink>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Link)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmLink> {
    val squareOpen = foundInfo[LinkFinder.SubIndex.SquareOpen]
    val label = foundInfo[LinkFinder.SubIndex.Label]
    val url = foundInfo[LinkFinder.SubIndex.Url]

    val isSilent = (input.substring(squareOpen.range) == "?[")
    val urlText = input.substring(url.range)
    val labelContents = NodeFactoryUtils.createNodes(input, label.sub, MfmNodeAttribute.setOfInline)
    if (labelContents.isEmpty()) {
      return failure()
    }

    val node = MfmLink(isSilent, urlText, labelContents)
    return success(node, foundInfo)
  }
}