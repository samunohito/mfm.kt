package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.finder.FullFinder
import com.github.samunohito.mfm.api.finder.IRecursiveFinderContext
import com.github.samunohito.mfm.api.finder.SimpleFinder
import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.utils.NodeFactoryUtils

object Mfm {
  /**
   * Generates a MfmNode tree from the MFM string.
   */
  @JvmStatic
  fun parse(input: String, startAt: Int = 0): List<IMfmNode> {
    val findResult = FullFinder(context = IRecursiveFinderContext.Impl()).find(input, startAt)
    if (!findResult.success) {
      return listOf()
    }

    return NodeFactoryUtils.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfAll)
  }

  /**
   * Generates a MfmSimpleNode tree from the MFM string.
   */
  @JvmStatic
  fun parseSimple(input: String, startAt: Int = 0): List<IMfmNode> {
    val findResult = SimpleFinder(context = IRecursiveFinderContext.Impl()).find(input, startAt)
    if (!findResult.success) {
      return listOf()
    }

    return NodeFactoryUtils.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfSimple)
  }
}