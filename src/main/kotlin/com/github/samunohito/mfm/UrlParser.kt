package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmUrl

class UrlParser : SimpleParserBase<MfmUrl>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Url)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmUrl> {
    return success(MfmUrl(input.substring(foundInfo.range), false), foundInfo)
  }
}