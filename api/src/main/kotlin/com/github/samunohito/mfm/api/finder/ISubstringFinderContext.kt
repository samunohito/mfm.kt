package com.github.samunohito.mfm.api.finder

interface ISubstringFinderContext {
  var excludeFinders: Set<Class<out ISubstringFinder>>

  data class Impl(
    override var excludeFinders: Set<Class<out ISubstringFinder>> = emptySet()
  ) : ISubstringFinderContext
}