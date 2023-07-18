package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.FnFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmFn
import com.github.samunohito.mfm.node.MfmNest

class FnNodeFactory : SimpleNodeFactoryBase<MfmFn>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Fn)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmFn> {
    val content = sliceContent(input, foundInfo)
    if (!content.success) {
      return failure()
    }

    val name = sliceName(input, foundInfo)
    val args = sliceArgs(input, foundInfo)

    val node = MfmFn(name, args, content.node.children.filterIsInstance(IMfmInline::class.java))
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

  private fun sliceContent(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<MfmNest> {
    val content = foundInfo[FnFinder.SubIndex.Content]
    return InlineNodeFactory().parse(input, content)
  }
}