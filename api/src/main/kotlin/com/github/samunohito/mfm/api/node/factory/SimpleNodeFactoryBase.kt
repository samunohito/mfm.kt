package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.IMfmNode

abstract class SimpleNodeFactoryBase<T : IMfmNode> : INodeFactory<T> {
  protected abstract val supportFoundTypes: Set<FoundType>

  override fun create(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<T> {
    if (!supportFoundTypes.contains(foundInfo.type)) {
      return failure()
    }

    return doCreate(input, foundInfo)
  }

  protected abstract fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<T>
}