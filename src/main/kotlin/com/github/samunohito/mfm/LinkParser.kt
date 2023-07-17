package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.LinkFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmLink

class LinkParser : SimpleParserBase<MfmLink, LinkFinder>() {
  override val finder = LinkFinder()
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Link)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmLink> {
    val squareOpen = foundInfo[LinkFinder.SubIndex.squareOpen]
    val label = foundInfo[LinkFinder.SubIndex.label]
    val url = foundInfo[LinkFinder.SubIndex.url]
  }

}