package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmPlain
import com.github.samunohito.mfm.api.node.MfmText

class PlainTagNodeFactory : SimpleNodeFactoryBase<MfmPlain>() {
  override val supportFoundTypes = setOf(FoundType.PlainTag)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmPlain> {
    val textNode = MfmText(input.substring(foundInfo.range))
    return success(MfmPlain(listOf(textNode)), foundInfo)
  }
}