package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SearchFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmSearch

class SearchNodeFactory : SimpleNodeFactoryBase<MfmSearch>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Search)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmSearch> {
    val query = input.substring(foundInfo[SearchFinder.SubIndex.Query].range)
    val space = input.substring(foundInfo[SearchFinder.SubIndex.Space].range)
    val button = input.substring(foundInfo[SearchFinder.SubIndex.Button].range)

    return success(MfmSearch(query, "${query}${space}${button}"), foundInfo)
  }
}