package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.FnFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.MfmFn
import com.github.samunohito.mfm.node.MfmNodeAttribute
import com.github.samunohito.mfm.node.factory.utils.NodeFactoryUtils

class FnNodeFactory : SimpleNodeFactoryBase<MfmFn>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Fn)

  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmFn> {
    val contents = sliceContent(input, foundInfo)
    if (contents.isEmpty()) {
      return failure()
    }

    val name = sliceName(input, foundInfo)
    val args = sliceArgs(input, foundInfo)

    val node = MfmFn(name, args, contents)
    return success(node, foundInfo)
  }

  private fun sliceName(input: String, foundInfo: SubstringFoundInfo): String {
    val name = foundInfo[FnFinder.SubIndex.Name]
    return input.substring(name.range)
  }

  private fun sliceArgs(input: String, foundInfo: SubstringFoundInfo): Map<String, Any> {
    val args = foundInfo[FnFinder.SubIndex.Args]
    return args.sub.asSequence()
      .map { input.substring(it.range) }
      .map { it.split("=") }
      .associate { (key, value) -> key to value }
  }

  private fun sliceContent(input: String, foundInfo: SubstringFoundInfo): List<IMfmNode> {
    val content = foundInfo[FnFinder.SubIndex.Content]
    return NodeFactoryUtils.createNodes(input, content.sub, MfmNodeAttribute.setOfInline)
  }
}