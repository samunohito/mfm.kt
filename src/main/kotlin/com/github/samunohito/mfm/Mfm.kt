package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.FullFinder
import com.github.samunohito.mfm.finder.SimpleFinder
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.IMfmNodeNestable
import com.github.samunohito.mfm.node.factory.FullNodeFactory
import com.github.samunohito.mfm.node.factory.SimpleNodeFactory
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

object Mfm {
  @JvmStatic
  fun parse(input: String): List<IMfmNode> {
    val findResult = FullFinder().find(input)
    if (!findResult.success) {
      return listOf()
    }

    val factoryResult = FullNodeFactory().create(input, findResult.foundInfo)
    if (!factoryResult.success) {
      return listOf()
    }

    return (factoryResult.node as IMfmNodeNestable<*>).children
  }

  @JvmStatic
  fun parseSimple(input: String): List<IMfmNode> {
    val findResult = SimpleFinder().find(input)
    if (!findResult.success) {
      return listOf()
    }

    val u = NodeFactoryUtils.recursiveInline2(input, findResult.foundInfo)

    return (u as IMfmNodeNestable<*>).children
  }
}