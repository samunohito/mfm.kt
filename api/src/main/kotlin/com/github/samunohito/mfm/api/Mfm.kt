package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.finder.FullFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.SimpleFinder
import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.NodeFactory
import com.github.samunohito.mfm.api.node.factory.internal.INodeFactoryContext

object Mfm {
  /**
   * Generates a MfmNode tree from the MFM string.
   */
  @JvmStatic
  fun parse(input: String, maxNestLevel: Int = 20): List<IMfmNode> {
    val finderContext = ISubstringFinderContext.Impl()
    val findResult = FullFinder().find(input, 0, finderContext)
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
    val finderContext = ISubstringFinderContext.Impl()
    val findResult = SimpleFinder().find(input, 0, finderContext)
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