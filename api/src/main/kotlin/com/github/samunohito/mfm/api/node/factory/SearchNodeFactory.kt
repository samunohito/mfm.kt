package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.SearchFinder
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.MfmSearch

class SearchNodeFactory : SimpleNodeFactoryBase<MfmSearch>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Search)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmSearch> {
    val query = input.substring(foundInfo[SearchFinder.SubIndex.Query].range)
    val space = input.substring(foundInfo[SearchFinder.SubIndex.Space].range)
    val button = input.substring(foundInfo[SearchFinder.SubIndex.Button].range)

    return success(MfmSearch(query, "${query}${space}${button}"), foundInfo)
  }
}