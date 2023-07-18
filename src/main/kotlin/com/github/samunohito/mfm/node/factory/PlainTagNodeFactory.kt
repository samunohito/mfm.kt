package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmPlain
import com.github.samunohito.mfm.node.MfmText

class PlainTagNodeFactory : SimpleNodeFactoryBase<MfmPlain>() {
  override val supportFoundTypes = setOf(FoundType.PlainTag)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmPlain> {
    val textNode = MfmText(input.substring(foundInfo.range))
    return success(MfmPlain(listOf(textNode)), foundInfo)
  }
}