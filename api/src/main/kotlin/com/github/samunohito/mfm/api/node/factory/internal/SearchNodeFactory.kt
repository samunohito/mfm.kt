package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmSearch
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo
import com.github.samunohito.mfm.api.parser.block.SearchParser
import com.github.samunohito.mfm.api.parser.core.FoundType

object SearchNodeFactory : SimpleNodeFactoryBase<MfmSearch>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Search)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmSearch> {
    val query = input.substring(foundInfo[SearchParser.SubIndex.Query].contentRange)
    val space = input.substring(foundInfo[SearchParser.SubIndex.Space].contentRange)
    val button = input.substring(foundInfo[SearchParser.SubIndex.Button].contentRange)

    return success(MfmSearch(query, "${query}${space}${button}"), foundInfo)
  }
}