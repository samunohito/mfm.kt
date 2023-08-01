package com.github.samunohito.mfm.api.finder.inline

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder

/**
 * An [ISubstringFinder] implementation for detecting "swaying character" syntax.
 * The string enclosed by "***" will be the search result.
 *
 * ### Notes
 * - Apply [InlineFinder] to the content again to recursively detect inline syntax.
 * - Any character and newline can be used in the content.
 * - The content cannot be left empty.
 */
object BigFinder : ISubstringFinder {
  private val mark = StringFinder("***")
  private val finder = SequentialFinder(
    mark,
    InlineFinder(mark),
    mark
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = finder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.Big,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}