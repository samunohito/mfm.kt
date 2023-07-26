@file:Suppress("DuplicatedCode")

package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmItalic
import com.github.samunohito.mfm.api.node.MfmText

class ItalicAstaNodeFactory : SimpleNodeFactoryBase<MfmItalic>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.ItalicAsta)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmItalic> {
    return success(MfmItalic(listOf(MfmText(input.substring(foundInfo.range)))), foundInfo)
  }
}