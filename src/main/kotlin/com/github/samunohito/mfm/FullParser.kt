package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.FoundType

class FullParser : RecursiveParserBase() {
  override val supportFoundTypes = setOf(FoundType.Full)
}