package com.github.samunohito.mfm.api.finder.inline

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.SpaceFinder

/**
 * An [ISubstringFinder] implementation for detecting "bold" syntax.
 * The string enclosed by "__" will be the search result.
 *
 * ### Notes
 * - Only allow characters matching the regex pattern of [a-z0-9 \t] (case-insensitive)
 * - The Content cannot be left empty.
 */
object BoldUnderFinder : ISubstringFinder {
  private val mark = StringFinder("__")
  private val boldUnderFinder = SequentialFinder(
    mark,
    AlternateScanFinder.ofWhile(RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceFinder),
    mark
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = boldUnderFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.BoldUnder,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}