package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmNode

abstract class SimpleParserBase<T : IMfmNode> : IParser<T> {
  protected abstract val supportFoundTypes: Set<FoundType>

  override fun parse(input: String, foundInfo: SubstringFoundInfo): IParserResult<T> {
    if (!supportFoundTypes.contains(foundInfo.type)) {
      return failure()
    }

    return doParse(input, foundInfo)
  }

  protected abstract fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<T>
}