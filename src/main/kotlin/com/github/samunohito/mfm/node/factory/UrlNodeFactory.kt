package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmUrl

class UrlNodeFactory : SimpleNodeFactoryBase<MfmUrl>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Url)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmUrl> {
    return success(MfmUrl(input.substring(foundInfo.range), false), foundInfo)
  }
}