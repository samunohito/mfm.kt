package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.IMfmNodeNestable
import com.github.samunohito.mfm.node.MfmNest

abstract class RecursiveParserBase : SimpleParserBase<MfmNest>() {
  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmNest> {
    val nest = MfmNest(listOf(parseNestable(input, foundInfo)))
    return success(nest, foundInfo)
  }

  private fun parseNestable(input: String, foundInfo: SubstringFoundInfo): IMfmNode {
    val parser = ParserFactory.get(foundInfo.type)
    val parseResult = parser.parse(input, foundInfo)

    val node = parseResult.node
    if (node is IMfmNodeNestable<*>) {
      val children = foundInfo.sub.map { parseNestable(input, it) }
      return node.addChild(children)
    }

    return node
  }
}