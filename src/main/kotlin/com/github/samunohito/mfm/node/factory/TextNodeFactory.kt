package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmText

class TextNodeFactory : SimpleNodeFactoryBase<MfmText>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Text)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmText> {
    return success(MfmText(input.substring(foundInfo.range)), foundInfo)
  }
}