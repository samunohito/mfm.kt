package com.github.samunohito.mfm.api.node.factory

interface INodeFactoryContext {
  val maximumNestLevel: Int
  var nestLevel: Int

  data class Impl(
    override val maximumNestLevel: Int = 20,
    override var nestLevel: Int = 0
  ) : INodeFactoryContext
}