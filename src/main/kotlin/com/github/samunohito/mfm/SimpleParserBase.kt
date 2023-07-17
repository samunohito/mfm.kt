package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmNode

abstract class SimpleParserBase<T : IMfmNode<*>, U : ISubstringFinder> : IParser<T> {
  protected abstract val finder: U
  protected abstract val supportFoundTypes: Set<FoundType>

  override fun parse(input: String, startAt: Int): IParserResult<T> {
    val result = finder.find(input, startAt)
    return parse(input, result.foundInfo)
  }

  override fun parse(input: String, foundInfo: SubstringFoundInfo): IParserResult<T> {
    if (!supportFoundTypes.contains(foundInfo.type)) {
      return failure()
    }

    return doParse(input, foundInfo)
  }

  protected abstract fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<T>
}