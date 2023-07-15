package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType

data class SubstringFoundInfo(
  val foundType: FoundType,
  val range: IntRange,
  val next: Int,
  val sub: List<SubstringFoundInfo>,
) {
  constructor(foundType: FoundType, info: SubstringFoundInfo) : this(foundType, info.range, info.next, info.sub)

  companion object {
    val EMPTY = SubstringFoundInfo(FoundType.Unknown, IntRange.EMPTY, Int.MIN_VALUE, emptyList())
  }
}