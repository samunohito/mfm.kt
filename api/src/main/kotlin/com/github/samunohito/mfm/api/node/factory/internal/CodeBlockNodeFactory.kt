package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmBlockCode
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.block.CodeBlockParser
import com.github.samunohito.mfm.api.parser.core.FoundType

object CodeBlockNodeFactory : SimpleNodeFactoryBase<MfmBlockCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.CodeBlock)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmBlockCode> {
    val langResult = foundInfo[CodeBlockParser.SubIndex.Lang]
    val codeResult = foundInfo[CodeBlockParser.SubIndex.Code]

    val code = input.substring(codeResult.contentRange)
    val node = if (langResult != SubstringFoundInfo.EMPTY) {
      MfmBlockCode(code, input.substring(langResult.contentRange))
    } else {
      MfmBlockCode(code)
    }

    return success(node, foundInfo)
  }
}