package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType

/**
 * Represents information about a found substring.
 *
 * @property type The type of the found substring.
 * @property overallRange The range of the found substring in the overall string. The "whole string" is the range that includes the format string (e.g. symbols such as #, @, *, etc.).
 * @property contentRange The range of the found substring in the actual content. The "actual content" is a range that does not contain format strings (e.g. symbols like #, @, *, etc.).
 * @property resumeIndex The restart index of the found substring. This index makes it easier to restart searches.
 * @property nestedInfos The list of nested [SubstringFoundInfo] objects.
 */
data class SubstringFoundInfo(
  val type: FoundType,
  val overallRange: IntRange,
  val contentRange: IntRange,
  val resumeIndex: Int,
  val nestedInfos: List<SubstringFoundInfo> = listOf()
) {
  companion object {
    /**
     * A [SubstringFoundInfo] object to treat as empty
     */
    val EMPTY = SubstringFoundInfo(
      FoundType.Empty,
      IntRange.EMPTY,
      IntRange.EMPTY,
      Int.MIN_VALUE,
      emptyList()
    )
  }

  /**
   * Creates a new [SubstringFoundInfo] object with the specified parameters.
   */
  constructor(type: FoundType, info: SubstringFoundInfo) : this(
    type,
    info.overallRange,
    info.contentRange,
    info.resumeIndex,
    info.nestedInfos
  )

  /**
   * Get the element at [index] in [nestedInfos].
   * This is to support a notation for simpler access to [nestedInfos].
   */
  operator fun get(index: Int): SubstringFoundInfo {
    return nestedInfos[index]
  }

  /**
   * Get the element at [index] in [nestedInfos].
   * This is to support a notation for simpler access to [nestedInfos].
   */
  operator fun get(index: ISubIndex): SubstringFoundInfo {
    return nestedInfos[index.index]
  }
}

/**
 * Represents an index used for accessing nested [SubstringFoundInfo] objects.
 */
interface ISubIndex {
  /**
   * The index value.
   */
  val index: Int
}
