package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.FoundType

class SimpleParser : RecursiveParserBase() {
  override val supportFoundTypes = setOf(FoundType.Simple)
}