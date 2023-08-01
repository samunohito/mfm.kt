package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo

interface INodeFactory<T : IMfmNode> {
  fun create(input: String, foundInfo: SubstringFoundInfo, context: INodeFactoryContext): IFactoryResult<T>
}