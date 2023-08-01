package com.github.samunohito.mfm.api.parser

/**
 * This is an interface definition for values used to exchange special parameters between [IMfmParser]s.
 */
interface IMfmParserContext {
  /**
   * Used in [RecursiveParserBase] implementations.
   * Implementations of [IMfmParser] registered with this [Set] are not used during parsing.
   */
  var excludeParsers: Set<Class<out IMfmParser>>

  data class Impl(
    override var excludeParsers: Set<Class<out IMfmParser>> = emptySet()
  ) : IMfmParserContext
}