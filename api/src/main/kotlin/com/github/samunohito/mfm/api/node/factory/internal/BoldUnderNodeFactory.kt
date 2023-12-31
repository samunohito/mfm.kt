@file:Suppress("DuplicatedCode")

package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmBold
import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.core.FoundType

object BoldUnderNodeFactory : SimpleNodeFactoryBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldUnder)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmBold> {
    val contents = input.substring(foundInfo.contentRange)
    val textNode = MfmText(contents)
    return success(MfmBold(listOf(textNode)), foundInfo)
  }
}