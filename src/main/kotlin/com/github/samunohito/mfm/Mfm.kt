package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.FullFinder
import com.github.samunohito.mfm.finder.InlineFinder
import com.github.samunohito.mfm.finder.SimpleFinder
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.MfmNodeAttribute
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

object Mfm {
  @JvmStatic
  fun parse(input: String, startAt: Int = 0): List<IMfmNode> {
    val findResult = FullFinder().find(input, startAt)
    if (!findResult.success) {
      return listOf()
    }

    return NodeFactoryUtils.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfAll)
  }

  @JvmStatic
  fun parseInline(input: String, startAt: Int): List<IMfmNode> {
    val findResult = InlineFinder().find(input, startAt)
    if (!findResult.success) {
      return listOf()
    }

    return NodeFactoryUtils.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfInline)
  }

  @JvmStatic
  fun parseSimple(input: String, startAt: Int = 0): List<IMfmNode> {
    val findResult = SimpleFinder().find(input, startAt)
    if (!findResult.success) {
      return listOf()
    }

    return NodeFactoryUtils.createNodes(input, findResult.foundInfo.sub, MfmNodeAttribute.setOfSimple)
  }
}