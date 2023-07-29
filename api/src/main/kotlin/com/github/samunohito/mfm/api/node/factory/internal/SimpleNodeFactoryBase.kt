package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.IMfmNode

abstract class SimpleNodeFactoryBase<T : IMfmNode> : INodeFactory<T> {
  protected abstract val supportFoundTypes: Set<FoundType>

  override fun create(input: String, foundInfo: SubstringFoundInfo, context: INodeFactoryContext): IFactoryResult<T> {
    if (!supportFoundTypes.contains(foundInfo.type)) {
      return failure()
    }

    if (context.nestLevel > context.maximumNestLevel) {
      return failure()
    }

    return doCreate(input, foundInfo, context)
  }

  protected abstract fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<T>
}