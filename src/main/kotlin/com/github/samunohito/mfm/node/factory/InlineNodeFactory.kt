package com.github.samunohito.mfm.node.factory

import com.github.samunohito.mfm.finder.core.FoundType

class InlineNodeFactory : RecursiveNodeFactoryBase() {
  override val supportFoundTypes = setOf(FoundType.Inline)
}