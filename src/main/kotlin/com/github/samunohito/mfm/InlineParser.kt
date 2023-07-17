package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.InlineFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.AlternateFinder
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.IMfmNodeNestable

class InlineParser(
  terminate: ISubstringFinder
) : SimpleParserBase<IMfmNode<*>, InlineFinder>() {
  override val finder = InlineFinder(AlternateFinder(terminate, LineEndFinder))
  override val supportFoundTypes = setOf(FoundType.Inline)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<IMfmNode<*>> {
    return success(parseNestable(input, foundInfo), foundInfo)
  }

  private fun parseNestable(input: String, foundInfo: SubstringFoundInfo): IMfmNode<*> {
    val parser = ParserFactory.get(foundInfo.type)
    val parseResult = parser.parse(input, foundInfo)

    val node = parseResult.node
    if (node is IMfmNodeNestable<*, *>) {
      val children = foundInfo.sub.map { parseNestable(input, it) }
      return node.addChild(children)
    }

    return node
  }
}