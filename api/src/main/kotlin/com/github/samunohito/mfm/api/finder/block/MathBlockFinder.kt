package com.github.samunohito.mfm.api.finder.block

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

/**
 * An [ISubstringFinder] implementation for detecting "math block" syntax.
 * The string enclosed by "\\[" and "\\]" will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 * - "\\[" must be the beginning of the line.
 * - "\\]" must be the end of the line.
 * - Leading and trailing spaces and newlines are trimmed.
 */
object MathBlockFinder : ISubstringFinder {
  private val open = StringFinder("\\[")
  private val close = StringFinder("\\]")
  private val mathBlockFinder = SequentialFinder(
    NewLineFinder.optional(),
    LineBeginFinder,
    open,
    NewLineFinder.optional(),
    SequentialScanFinder.ofUntil(NewLineFinder.optional(), close),
    NewLineFinder.optional(),
    close,
    LineEndFinder,
    NewLineFinder.optional()
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = mathBlockFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[4]
    return success(
      FoundType.MathBlock,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}