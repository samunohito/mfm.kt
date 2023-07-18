package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.core.FoundType

class SimpleNodeFactory : RecursiveNodeFactoryBase() {
  override val supportFoundTypes = setOf(FoundType.Simple)
}