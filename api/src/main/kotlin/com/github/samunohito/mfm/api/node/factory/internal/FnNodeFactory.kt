package com.github.samunohito.mfm.api.node.factory.internal

import com.github.samunohito.mfm.api.finder.FnFinder
import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmFn
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.factory.NodeFactory

object FnNodeFactory : SimpleNodeFactoryBase<MfmFn>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Fn)

  override fun doCreate(
    input: String,
    foundInfo: SubstringFoundInfo,
    context: INodeFactoryContext
  ): IFactoryResult<MfmFn> {
    val contents = sliceContent(input, foundInfo, context)
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
    return input.substring(name.contentRange)
  }

  private fun sliceArgs(input: String, foundInfo: SubstringFoundInfo): Map<String, Any> {
    val args = foundInfo[FnFinder.SubIndex.Args]
    return args.nestedInfos.asSequence()
      .map { input.substring(it.contentRange) }
      .map { it.split("=") }
      .associate {
        if (it.size == 1) {
          it[0] to Unit
        } else {
          it[0] to it[1]
        }
      }
  }

  private fun sliceContent(input: String, foundInfo: SubstringFoundInfo, context: INodeFactoryContext): List<IMfmNode> {
    val content = foundInfo[FnFinder.SubIndex.Content]
    return NodeFactory.createNodes(input, content.nestedInfos, MfmNodeAttribute.setOfInline, context)
  }
}