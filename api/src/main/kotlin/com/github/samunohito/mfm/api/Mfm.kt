package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.finder.FullFinder
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
    val findResult = FullFinder().find(input, startAt)
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
    val findResult = SimpleFinder().find(input, startAt)
    if (!findResult.success) {
      return listOf()
    }

    return NodeFactoryUtils.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfSimple)
  }
}