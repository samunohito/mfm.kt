package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmCenter
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class CenterTagNodeFactory : SimpleNodeFactoryBase<MfmCenter>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.CenterTag)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmCenter> {
    val result = NodeFactoryUtils.recursiveInline(input, foundInfo)
    if (result.isEmpty()) {
      return failure()
    }

    return success(MfmCenter(result), foundInfo)
  }
}