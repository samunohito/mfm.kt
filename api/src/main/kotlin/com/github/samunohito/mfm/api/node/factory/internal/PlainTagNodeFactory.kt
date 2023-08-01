package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmPlain
import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object PlainTagNodeFactory : SimpleNodeFactoryBase<MfmPlain>() {
  override val supportFoundTypes = setOf(FoundType.PlainTag)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmPlain> {
    val textNode = MfmText(input.substring(foundInfo.contentRange))
    return success(MfmPlain(listOf(textNode)), foundInfo)
  }
}