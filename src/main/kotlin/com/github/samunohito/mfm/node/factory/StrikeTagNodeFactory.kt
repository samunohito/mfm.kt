package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmStrike
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class StrikeTagNodeFactory : SimpleNodeFactoryBase<MfmStrike>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.StrikeTag)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmStrike> {
    val result = NodeFactoryUtils.recursiveInline(input, foundInfo)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmStrike(result), foundInfo)
  }
}