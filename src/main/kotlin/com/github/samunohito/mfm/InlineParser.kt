package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.FoundType

class InlineParser : RecursiveParserBase() {
  override val supportFoundTypes = setOf(FoundType.Inline)
}