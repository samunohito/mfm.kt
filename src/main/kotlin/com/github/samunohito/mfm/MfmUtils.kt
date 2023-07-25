package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.*

object MfmUtils {
  /**
   * Generates MFM string from the MfmNode tree.
   */
  fun stringify(nodes: Iterable<IMfmNode>): String {
    // 文脈に合わせて改行を追加する。
    // none -> inline   : No
    // none -> block    : No
    // inline -> inline : No
    // inline -> block  : Yes
    // block -> inline  : Yes
    // block -> block   : Yes

    val resultNodes = ArrayList<IMfmNode>()
    var prevNodeType: MfmNodeType = MfmNodeType.None
    for (node in nodes) {
      if (node.type.attributes.contains(MfmNodeAttribute.Block)) {
        if (prevNodeType != MfmNodeType.None) {
          resultNodes.add(MfmText("\n"))
        }
      } else {
        if (prevNodeType != MfmNodeType.None && !prevNodeType.attributes.contains(MfmNodeAttribute.Inline)) {
          resultNodes.add(MfmText("\n"))
        }
      }

      resultNodes.add(node)
      prevNodeType = node.type
    }

    return resultNodes.joinToString("") { it.stringify() }
  }

  /**
   * Inspects the MfmNode tree.
   */
  fun inspect(node: IMfmNode, action: (IMfmNode) -> Unit) {
    action(node)
    if (node is IMfmNodeChildrenHolder) {
      node.children.forEach { inspect(it, action) }
    }
  }

  /**
   * Inspects the MfmNode tree.
   */
  fun inspect(nodes: Iterable<IMfmNode>, action: (IMfmNode) -> Unit) {
    nodes.forEach { inspect(it, action) }
  }

  /**
   * Inspects the MfmNode tree and returns as an array the nodes that match the conditions
   * of the predicate function.
   */
  fun extract(nodes: Iterable<IMfmNode>, predicate: (IMfmNode) -> Boolean): List<IMfmNode> {
    val result = mutableListOf<IMfmNode>()

    inspect(nodes) {
      if (predicate(it)) {
        result.add(it)
      }
    }

    return result
  }
}