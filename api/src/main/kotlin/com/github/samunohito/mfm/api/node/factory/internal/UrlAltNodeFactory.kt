package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmUrl
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object UrlAltNodeFactory : SimpleNodeFactoryBase<MfmUrl>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.UrlAlt)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmUrl> {
    return success(MfmUrl(input.substring(foundInfo.contentRange), true), foundInfo)
  }
}