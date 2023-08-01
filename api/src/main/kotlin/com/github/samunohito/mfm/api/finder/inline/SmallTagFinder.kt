package com.github.samunohito.mfm.api.finder.inline

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder

/**
 * An [ISubstringFinder] implementation for detecting "small" syntax.
 * The string enclosed by <small> tags will be the search result.
 *
 * ### Notes
 * - Apply [InlineFinder] to the content again to recursively detect inline syntax.
 * - Any character and newline can be used in the content.
 * - The content cannot be left empty.
 */
object SmallTagFinder : ISubstringFinder {
  private val open = StringFinder("<small>")
  private val close = StringFinder("</small>")
  private val finder = SequentialFinder(
    open,
    InlineFinder(close),
    close
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = finder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.SmallTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}