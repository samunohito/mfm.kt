package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmText

class TextNodeFactory : SimpleNodeFactoryBase<MfmText>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Text)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmText> {
    return success(MfmText(input.substring(foundInfo.range)), foundInfo)
  }
}