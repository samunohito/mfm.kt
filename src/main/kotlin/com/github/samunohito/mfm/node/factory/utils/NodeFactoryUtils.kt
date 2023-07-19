package com.github.samunohito.mfm.node.factory.utils

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.*

object NodeFactoryUtils {
  fun recursiveInline(
    input: String,
    foundInfo: SubstringFoundInfo,
  ): List<IMfmInline> {
    when (foundInfo.type) {
      FoundType.Simple, FoundType.Inline, FoundType.Full -> {

      }

      else -> {
        val factory = NodeFactories.get(foundInfo.type)
        val result = factory.create(input, foundInfo)
        if (!result.success || result.node !is IMfmInline) {
          return listOf()
        }

        return emptyList()
      }
    }

    val resultNodes = ArrayList<IMfmInline>(foundInfo.sub.size)
    for (subInfo in foundInfo.sub) {
      when (subInfo.type) {
        FoundType.Simple, FoundType.Inline, FoundType.Full -> {
          resultNodes.add(MfmNest(recursiveInline(input, subInfo)))
        }

        else -> {
          val factory = NodeFactories.get(subInfo.type)
          val result = factory.create(input, subInfo)
          if (!result.success || result.node !is IMfmInline) {
            continue
          }

          resultNodes.add(result.node as IMfmInline)
        }
      }
    }

    return resultNodes
  }

  fun recursiveInline2(
    input: String,
    foundInfo: SubstringFoundInfo,
  ): IMfmInline {
    when (foundInfo.type) {
      FoundType.Simple, FoundType.Inline, FoundType.Full -> {
        val node = MfmNest()
        val subNodes = foundInfo.sub.map { recursiveInline2(input, it) }
        return node.addChild(subNodes)
      }

      else -> {
        val factory = NodeFactories.get(foundInfo.type)
        val result = factory.create(input, foundInfo)
        if (!result.success) {
          return MfmEmpty
        }

        val node = result.node
        if (result.node !is IMfmInline) {
          return MfmEmpty
        }

        return if (node is IMfmNodeNestable<*>) {
          val subNodes = foundInfo.sub.map { recursiveInline2(input, it) }
          node.addChild(subNodes) as IMfmInline
        } else {
          node as IMfmInline
        }
      }
    }
  }

  fun recursiveFull(
    input: String,
    foundInfo: SubstringFoundInfo,
  ): List<IMfmNode> {
    val resultNodes = ArrayList<IMfmNode>(foundInfo.sub.size)
    for (subInfo in foundInfo.sub) {
      when (subInfo.type) {
        FoundType.Simple, FoundType.Inline, FoundType.Full -> {
          resultNodes.add(MfmNest(recursiveInline(input, subInfo)))
        }

        else -> {
          val factory = NodeFactories.get(subInfo.type)
          val result = factory.create(input, subInfo)
          if (!result.success) {
            continue
          }

          resultNodes.add(result.node)
        }
      }
    }

    return resultNodes
  }
}