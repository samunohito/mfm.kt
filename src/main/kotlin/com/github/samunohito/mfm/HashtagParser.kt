package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmHashtag

class HashtagParser : SimpleParserBase<MfmHashtag>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Hashtag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmHashtag> {
    return success(MfmHashtag(input.substring(foundInfo.range)), foundInfo)
  }
}