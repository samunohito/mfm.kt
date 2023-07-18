package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.node.IMfmNode

interface IFactoryResult<T : IMfmNode> {
  val success: Boolean
  val node: T
  val foundInfo: SubstringFoundInfo
}

private class Impl<T : IMfmNode>(
  override val success: Boolean,
  node: T?,
  override val foundInfo: SubstringFoundInfo,
) : IFactoryResult<T> {
  override val node: T by lazy { node ?: error("node is null") }

  companion object {
    fun <T : IMfmNode> ofSuccess(node: T, foundInfo: SubstringFoundInfo): IFactoryResult<T> {
      return Impl(true, node, foundInfo)
    }

    fun <T : IMfmNode> ofFailure(): IFactoryResult<T> {
      return Impl(false, null, SubstringFoundInfo.EMPTY)
    }
  }
}

fun <T : IMfmNode> success(node: T, foundInfo: SubstringFoundInfo): IFactoryResult<T> {
  return Impl.ofSuccess(node, foundInfo)
}

fun <T : IMfmNode> failure(): IFactoryResult<T> {
  return Impl.ofFailure()
}