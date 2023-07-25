package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.CodeBlockFinder
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmBlockCode

class CodeBlockNodeFactory : SimpleNodeFactoryBase<MfmBlockCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.CodeBlock)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmBlockCode> {
    val langResult = foundInfo[CodeBlockFinder.SubIndex.Lang]
    val codeResult = foundInfo[CodeBlockFinder.SubIndex.Code]

    val code = input.substring(codeResult.range)
    val node = if (langResult != SubstringFoundInfo.EMPTY) {
      MfmBlockCode(code, input.substring(langResult.range))
    } else {
      MfmBlockCode(code)
    }

    return success(node, foundInfo)
  }
}