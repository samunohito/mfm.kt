package com.github.samunohito.mfm.node.factory.utils

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.MfmNest
import com.github.samunohito.mfm.node.MfmNodeAttribute
import com.github.samunohito.mfm.node.factory.IFactoryResult
import com.github.samunohito.mfm.node.factory.failure
import com.github.samunohito.mfm.node.factory.success

object NodeFactoryUtils {
  fun createNodes(
    input: String,
    foundInfos: List<SubstringFoundInfo>,
    allowNodeAttribute: Set<MfmNodeAttribute> = MfmNodeAttribute.setOfAll,
  ): List<IMfmNode> {
    return foundInfos.asSequence()
      .map { createNodes(input, it, allowNodeAttribute) }
      .filter { it.success }
      .map { it.node }
      .toList()
  }

  private fun createNodes(
    input: String,
    foundInfo: SubstringFoundInfo,
    allowNodeAttribute: Set<MfmNodeAttribute> = MfmNodeAttribute.setOfAll,
  ): IFactoryResult<IMfmNode> {
    when (foundInfo.type) {
      FoundType.Simple, FoundType.Inline, FoundType.Full -> {
        val results = foundInfo.sub.map { createNodes(input, it, allowNodeAttribute) }
        if (results.any { !it.success }) {
          return failure()
        }

        return success(MfmNest(results.map { it.node }), foundInfo)
      }

      else -> {
        val factory = NodeFactories.get(foundInfo.type)
        val result = factory.create(input, foundInfo)
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