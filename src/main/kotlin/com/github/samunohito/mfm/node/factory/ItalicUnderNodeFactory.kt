@file:Suppress("DuplicatedCode")

package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmItalic
import com.github.samunohito.mfm.node.MfmText

class ItalicUnderNodeFactory : SimpleNodeFactoryBase<MfmItalic>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.ItalicUnder)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmItalic> {
    val contents = input.substring(foundInfo.range)
    val textNode = MfmText(contents)
    return success(MfmItalic(listOf(textNode)), foundInfo)
  }
}