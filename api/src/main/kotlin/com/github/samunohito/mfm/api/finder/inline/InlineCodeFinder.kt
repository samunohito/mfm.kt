package com.github.samunohito.mfm.api.finder.inline

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

/**
 * An [ISubstringFinder] implementation for detecting "inline code" syntax.
 * The string enclosed by "`" will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 * - Any character and newline can be used in the content.
 * - The content cannot be left empty.
 * - The content cannot contain the character "`".
 */
object InlineCodeFinder : ISubstringFinder {
  private val mark = StringFinder("`")
  private val mark2 = StringFinder("´")
  private val inlineCodeFinder = SequentialFinder(
    mark,
    AlternateScanFinder.ofUntil(mark, mark2, NewLineFinder),
    mark
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = inlineCodeFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.InlineCode,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}