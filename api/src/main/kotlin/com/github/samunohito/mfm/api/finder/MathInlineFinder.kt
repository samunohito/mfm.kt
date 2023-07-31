package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

/**
 * An [ISubstringFinder] implementation for detecting "math inline" syntax.
 * The string enclosed by "\[" and "\]" will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 * - Content cannot be empty.
 * - The content cannot contain line breaks.
 */
object MathInlineFinder : ISubstringFinder {
  private val open = StringFinder(word = "\\(")
  private val close = StringFinder("\\)")
  private val mathInlineFinder = SequentialFinder(
    open,
    AlternateScanFinder.ofUntil(close, NewLineFinder),
    close
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = mathInlineFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.MathInline,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}