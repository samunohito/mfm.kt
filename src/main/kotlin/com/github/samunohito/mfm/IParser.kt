package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.node.IMfmNode

interface IParser<T : IMfmNode<*>> {
  fun parse(input: String, startAt: Int = 0): IParserResult<T>
  fun parse(input: String, foundInfo: SubstringFoundInfo): IParserResult<T>
}