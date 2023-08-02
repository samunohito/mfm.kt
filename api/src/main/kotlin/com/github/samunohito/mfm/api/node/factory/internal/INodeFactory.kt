package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.SubstringFoundInfo

/**
 * Create an object of [IMfmNode] from the parsing result of [IMfmParser].
 */
interface INodeFactory<T : IMfmNode> {
  /**
   * Create an object of [IMfmNode] from the parsing result of [IMfmParser].
   *
   * @param input The input string.
   * @param foundInfo The parsing result of [IMfmParser].
   * @param context The context of the factory.
   */
  fun create(input: String, foundInfo: SubstringFoundInfo, context: INodeFactoryContext): IFactoryResult<T>
}