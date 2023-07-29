package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.SearchFinder
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmSearch

object SearchNodeFactory : SimpleNodeFactoryBase<MfmSearch>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Search)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmSearch> {
    val query = input.substring(foundInfo[SearchFinder.SubIndex.Query].contentRange)
    val space = input.substring(foundInfo[SearchFinder.SubIndex.Space].contentRange)
    val button = input.substring(foundInfo[SearchFinder.SubIndex.Button].contentRange)

    return success(MfmSearch(query, "${query}${space}${button}"), foundInfo)
  }
}