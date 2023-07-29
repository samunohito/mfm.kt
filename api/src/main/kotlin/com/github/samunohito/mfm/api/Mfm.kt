package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.finder.FullFinder
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
    val findResult = FullFinder().find(input, 0)
    if (!findResult.success) {
      return listOf()
    }

    val context = INodeFactoryContext.Impl(maximumNestLevel = maxNestLevel)
    return NodeFactory.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfAll, context)
  }

  /**
   * Generates a MfmSimpleNode tree from the MFM string.
   */
  @JvmStatic
  fun parseSimple(input: String, maxNestLevel: Int = 20): List<IMfmNode> {
    val findResult = SimpleFinder().find(input, 0)
    if (!findResult.success) {
      return listOf()
    }

    val context = INodeFactoryContext.Impl(maximumNestLevel = maxNestLevel)
    return NodeFactory.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfSimple, context)
  }
}