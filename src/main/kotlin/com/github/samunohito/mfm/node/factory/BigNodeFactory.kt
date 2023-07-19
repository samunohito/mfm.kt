package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmFn
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class BigNodeFactory : SimpleNodeFactoryBase<MfmFn>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Fn)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmFn> {
    val result = NodeFactoryUtils.recursiveInline(input, foundInfo)
    if (result.isEmpty()) {
      return failure()
    }

    val node = MfmFn("tada", emptyMap(), result)
    return success(node, foundInfo)
  }
}