package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.node.IMfmNode

interface IParser<T : IMfmNode> {
  fun parse(input: String, foundInfo: SubstringFoundInfo): IParserResult<T>
}