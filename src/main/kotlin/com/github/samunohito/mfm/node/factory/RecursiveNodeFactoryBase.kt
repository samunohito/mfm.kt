package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.IMfmNodeNestable
import com.github.samunohito.mfm.node.MfmNest

abstract class RecursiveNodeFactoryBase : SimpleNodeFactoryBase<MfmNest>() {
  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmNest> {
    val nest = MfmNest(listOf(parseNestable(input, foundInfo)))
    return success(nest, foundInfo)
  }

  private fun parseNestable(input: String, foundInfo: SubstringFoundInfo): IMfmNode {
    val factory = NodeFactories.get(foundInfo.type)
    val result = factory.parse(input, foundInfo)

    val node = result.node
    if (node is IMfmNodeNestable<*>) {
      val children = foundInfo.sub.map { parseNestable(input, it) }
      return node.addChild(children)
    }

    return node
  }
}