package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.IMfmNode
import com.github.samunohito.mfm.node.IMfmNodeNestable
import com.github.samunohito.mfm.node.MfmNest

abstract class RecursiveNodeFactoryBase : SimpleNodeFactoryBase<IMfmNode>() {
  override fun doCreate(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<IMfmNode> {
    return doCreateInternal(input, foundInfo)
  }

  private fun doCreateInternal(input: String, foundInfo: SubstringFoundInfo): IFactoryResult<IMfmNode> {
    TODO()
//    return when (foundInfo.type) {
//      FoundType.Simple, FoundType.Inline, FoundType.Full -> {
//        val nestableResults = foundInfo.sub.map { doCreateInternal(input, it) }
//        if (nestableResults.any { !it.success }) {
//          failure()
//        } else {
//          success(MfmNest(nestableResults.map { it.node }), foundInfo)
//        }
//      }
//
//      else -> {
//        val factory = NodeFactories.get(foundInfo.type)
//        val result = factory.create(input, foundInfo)
//        if (!result.success) {
//          failure()
//        } else {
//          val node = result.node
//          if (node is IMfmNodeNestable<*>) {
//            val nestableResults = foundInfo.sub.map { doCreateInternal(input, it) }
//            if (nestableResults.any { !it.success }) {
//              failure()
//            } else {
//              success(node.addChild(nestableResults.map { it.node }), foundInfo)
//            }
//          } else {
//            success(node, foundInfo)
//          }
//        }
//      }
//    }
  }
}