@file:Suppress("DuplicatedCode")

package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmItalic
import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object ItalicUnderNodeFactory : SimpleNodeFactoryBase<MfmItalic>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.ItalicUnder)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmItalic> {
    return success(MfmItalic(listOf(MfmText(input.substring(foundInfo.contentRange)))), foundInfo)
  }
}