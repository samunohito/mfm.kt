package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo

object TextNodeFactory : INodeFactory<MfmText> {
  override fun create(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmText> {
    return success(MfmText(input.substring(foundInfo.contentRange)), foundInfo)
  }
}