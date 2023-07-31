package com.github.samunohito.mfm.api.finder

/**
 * This is an interface definition for values used to exchange special parameters between [ISubstringFinder]s.
 */
interface ISubstringFinderContext {
  var excludeFinders: Set<Class<out ISubstringFinder>>

  data class Impl(
    override var excludeFinders: Set<Class<out ISubstringFinder>> = emptySet()
  ) : ISubstringFinderContext
}