package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.SpaceFinder

/**
 * An [ISubstringFinder] implementation for detecting "italic" syntax.
 * The string enclosed by "_" will be the search result.
 *
 * ### Notes
 * - Only allow characters matching the regex pattern of [a-z0-9 \t] (case-insensitive)
 * - If the character before the starting symbol (the first "*") matches [a-z0-9] (case-insensitive), it is not recognized as italic formatting.
 * - The content cannot be left empty.
 */
@Suppress("DuplicatedCode")
object ItalicUnderFinder : ISubstringFinder {
  private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
  private val markFinder = StringFinder("_")
  private val italicUnderFinder = SequentialFinder(
    markFinder,
    AlternateScanFinder.ofWhile(RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceFinder),
    markFinder
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = italicUnderFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    // 直前が英数字だったら認識しない
    val beforeStr = input.substring(0, startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.ItalicUnder,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}