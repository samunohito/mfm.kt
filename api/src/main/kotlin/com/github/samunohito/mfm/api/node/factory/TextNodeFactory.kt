package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.node.MfmText

class TextNodeFactory : INodeFactory<MfmText> {
  override fun create(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmText> {
    return success(MfmText(input.substring(foundInfo.contentRange)), foundInfo)
  }
}