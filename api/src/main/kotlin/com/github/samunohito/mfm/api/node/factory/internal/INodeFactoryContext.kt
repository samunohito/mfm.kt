package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.MfmText

/**
 * The context of the factory.
 */
interface INodeFactoryContext {
  /**
   * The maximum nest level of the factory.
   */
  val maximumNestLevel: Int

  /**
   * The nest level of the factory.
   * If this number exceeds [maximumNestLevel], all parsing results below that level will be treated as [MfmText] nodes.
   */
  var nestLevel: Int

  data class Impl(
    override val maximumNestLevel: Int = 20,
    override var nestLevel: Int = 0,
  ) : INodeFactoryContext
}