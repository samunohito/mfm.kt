package com.github.samunohito.mfm.api.node.factory.utils

import com.github.samunohito.mfm.api.finder.SubstringFoundInfo
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.node.IMfmNode
import com.github.samunohito.mfm.api.node.MfmNest
import com.github.samunohito.mfm.api.node.MfmNodeAttribute
import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.node.factory.IFactoryResult
import com.github.samunohito.mfm.api.node.factory.INodeFactoryContext
import com.github.samunohito.mfm.api.node.factory.failure
import com.github.samunohito.mfm.api.node.factory.success

object NodeFactoryUtils {
  fun createNodes(
    input: String,
    foundInfos: List<SubstringFoundInfo>,
    allowNodeAttribute: Set<MfmNodeAttribute> = MfmNodeAttribute.setOfAll,
    context: INodeFactoryContext,
  ): List<IMfmNode> {
    return foundInfos.asSequence()
      .map { createNodes(input, it, allowNodeAttribute, context) }
      .filter { it.success }
      .map { it.node }
      .toList()
  }

  private fun createNodes(
    input: String,
    foundInfo: SubstringFoundInfo,
    allowNodeAttribute: Set<MfmNodeAttribute> = MfmNodeAttribute.setOfAll,
    context: INodeFactoryContext,
  ): IFactoryResult<IMfmNode> {
    // ネストが上限に達している場合は入力をそのままMfmTextとして扱い、これ以上ネストさせない
    if (context.nestLevel >= context.maximumNestLevel) {
      return success(MfmText(input.substring(foundInfo.fullRange)), foundInfo)
    }

    when (val foundType = foundInfo.type) {
      FoundType.Simple, FoundType.Inline, FoundType.Full -> {
        val results = foundInfo.sub.map { createNodes(input, it, allowNodeAttribute, context) }
        if (results.any { !it.success }) {
          return failure()
        }

        return success(MfmNest(results.map { it.node }), foundInfo)
      }

      else -> {
        val factory = NodeFactories.get(foundType)

        context.nestLevel++
        val result = factory.create(input, foundInfo, context)
        context.nestLevel--

        if (!result.success) {
          return failure()
        }

        val node = result.node
        if (!allowNodeAttribute.any { node.type.attributes.contains(it) }) {
          return failure()
        }

        return success(node, foundInfo)
      }
    }
  }
}