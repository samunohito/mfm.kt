package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.FoundType

object ParserFactory {
  fun get(foundType: FoundType): IParser<*> {
    // TODO: Implement
    return when (foundType) {
      FoundType.Link -> LinkParser()
      FoundType.MathBlock -> MathBlockParser()
      else -> throw IllegalArgumentException("Unknown FoundType: $foundType")
    }
  }
}