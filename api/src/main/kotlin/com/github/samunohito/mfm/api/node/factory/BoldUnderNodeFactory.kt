@file:Suppress("DuplicatedCode")

package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmBold
import com.github.samunohito.mfm.api.node.MfmText

class BoldUnderNodeFactory : SimpleNodeFactoryBase<MfmBold>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.BoldUnder)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmBold> {
    val contents = input.substring(foundInfo.range)
    val textNode = MfmText(contents)
    return success(MfmBold(listOf(textNode)), foundInfo)
  }
}