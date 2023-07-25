package com.github.samunohito.mfm.api.node.factory

import com.github.samunohito.mfm.api.finder.FnFinder
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmFn
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.utils.NodeFactoryUtils

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
      .associate {
        if (it.size == 1) {
          it[0] to Unit
        } else {
          it[0] to it[1]
        }
      }
  }

  private fun sliceContent(input: String, foundInfo: SubstringFoundInfo): List<IMfmNode> {
    val content = foundInfo[FnFinder.SubIndex.Content]
    return NodeFactoryUtils.createNodes(input, content.sub, MfmNodeAttribute.setOfInline)
  }
}