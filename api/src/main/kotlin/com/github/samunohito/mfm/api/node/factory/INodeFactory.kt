package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.node.IMfmNode

interface INodeFactory<T : IMfmNode> {
  fun create(input: String, foundInfo: SubstringFoundInfo, context: INodeFactoryContext): IFactoryResult<T>
}