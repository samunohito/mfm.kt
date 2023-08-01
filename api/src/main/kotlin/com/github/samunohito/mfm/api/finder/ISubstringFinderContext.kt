package com.github.samunohito.mfm.api.finder

/**
 * This is an interface definition for values used to exchange special parameters between [ISubstringFinder]s.
 */
interface ISubstringFinderContext {
  /**
   * Used in [RecursiveFinderBase] implementations.
   * Implementations of [ISubstringFinder] registered with this [Set] are not used during parsing.
   */
  var excludeFinders: Set<Class<out ISubstringFinder>>

  data class Impl(
    override var excludeFinders: Set<Class<out ISubstringFinder>> = emptySet()
  ) : ISubstringFinderContext
}