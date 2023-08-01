package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmUrl
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object UrlNodeFactory : SimpleNodeFactoryBase<MfmUrl>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Url)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmUrl> {
    return success(MfmUrl(input.substring(foundInfo.contentRange), false), foundInfo)
  }
}