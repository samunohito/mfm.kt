package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmHashtag

class HashtagNodeFactory : SimpleNodeFactoryBase<MfmHashtag>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Hashtag)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmHashtag> {
    return success(MfmHashtag(input.substring(foundInfo.contentRange)), foundInfo)
  }
}