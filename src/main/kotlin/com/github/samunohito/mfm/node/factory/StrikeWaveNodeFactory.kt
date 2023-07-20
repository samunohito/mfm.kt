package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmNodeAttribute
import com.github.samunohito.mfm.node.MfmStrike
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class StrikeWaveNodeFactory : SimpleNodeFactoryBase<MfmStrike>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.StrikeWave)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmStrike> {
    val result = NodeFactoryUtils.createNodes(input, foundInfo.sub, MfmNodeAttribute.setOfInline)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmStrike(result), foundInfo)
  }
}