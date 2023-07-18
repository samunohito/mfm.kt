package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.LinkFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmLink

class LinkNodeFactory : SimpleNodeFactoryBase<MfmLink>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Link)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmLink> {
    val squareOpen = foundInfo[LinkFinder.SubIndex.SquareOpen]
    val label = foundInfo[LinkFinder.SubIndex.Label]
    val url = foundInfo[LinkFinder.SubIndex.Url]

    val isSilent = (input.substring(squareOpen.range) == "?[")
    val urlText = input.substring(url.range)
    val labelResult = InlineNodeFactory().parse(input, label)
    val node = MfmLink(isSilent, urlText, listOf(labelResult.node))

    return success(node, foundInfo)
  }
}