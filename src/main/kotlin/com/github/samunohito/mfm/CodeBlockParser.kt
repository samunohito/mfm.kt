package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.CodeBlockFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmBlockCode

class CodeBlockParser : SimpleParserBase<MfmBlockCode>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.CodeBlock)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmBlockCode> {
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