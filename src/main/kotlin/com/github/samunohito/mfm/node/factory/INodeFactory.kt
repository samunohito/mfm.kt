package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.node.IMfmNode

interface INodeFactory<T : IMfmNode> {
  fun create(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<T>
}