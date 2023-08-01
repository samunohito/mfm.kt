package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.NodeFactory
import com.github.samunohito.mfm.api.node.factory.internal.INodeFactoryContext
import com.github.samunohito.mfm.api.parser.FullParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.SimpleParser

object Mfm {
  /**
   * Generates a MfmNode tree from the MFM string.
   */
  @JvmStatic
  fun parse(input: String, maxNestLevel: Int = 20): List<IMfmNode> {
    val finderContext = IMfmParserContext.Impl()
    val findResult = FullParser().find(input, 0, finderContext)
    if (!findResult.success) {
      return listOf()
    }

    val factoryContext = INodeFactoryContext.Impl(maximumNestLevel = maxNestLevel)
    return NodeFactory.createNodes(
      input,
      findResult.foundInfo.nestedInfos,
      factoryContext,
      MfmNodeAttribute.setOfAll
    )
  }

  /**
   * Generates a MfmSimpleNode tree from the MFM string.
   */
  @JvmStatic
  fun parseSimple(input: String, maxNestLevel: Int = 20): List<IMfmNode> {
    val finderContext = IMfmParserContext.Impl()
    val findResult = SimpleParser().find(input, 0, finderContext)
    if (!findResult.success) {
      return listOf()
    }

    val factoryContext = INodeFactoryContext.Impl(maximumNestLevel = maxNestLevel)
    return NodeFactory.createNodes(
      input,
      findResult.foundInfo.nestedInfos,
      factoryContext,
      MfmNodeAttribute.setOfSimple
    )
  }
}