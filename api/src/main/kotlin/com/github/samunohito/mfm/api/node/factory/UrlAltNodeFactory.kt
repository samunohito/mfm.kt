package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmUrl

class UrlAltNodeFactory : SimpleNodeFactoryBase<MfmUrl>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.UrlAlt)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmUrl> {
    return success(MfmUrl(input.substring(foundInfo.range), true), foundInfo)
  }
}