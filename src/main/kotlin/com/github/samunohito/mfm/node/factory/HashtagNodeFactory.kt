package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmHashtag

class HashtagNodeFactory : SimpleNodeFactoryBase<MfmHashtag>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Hashtag)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmHashtag> {
    return success(MfmHashtag(input.substring(foundInfo.range)), foundInfo)
  }
}