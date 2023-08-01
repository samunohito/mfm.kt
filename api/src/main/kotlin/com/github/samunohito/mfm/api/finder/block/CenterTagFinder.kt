package com.github.samunohito.mfm.api.finder.block

import com.github.samunohito.mfm.api.finder.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

/**
 * An [ISubstringFinder] implementation for detecting "center" syntax.
 * The string enclosed by <center> tags will be the search result.
 *
 * ### Notes
 * - Apply [InlineFinder] to the content again to recursively detect inline syntax.
 * - <center> must be the beginning of the line.
 * - </center> must be the end of the line.
 */
object CenterTagFinder : ISubstringFinder {
  private val open = StringFinder("<center>")
  private val close = StringFinder("</center>")
  private val finder = SequentialFinder(
    NewLineFinder.optional(),
    LineBeginFinder,
    open,
    NewLineFinder.optional(),
    InlineFinder(SequentialFinder(optional(), close)),
    NewLineFinder.optional(),
    close,
    LineEndFinder,
    NewLineFinder.optional(),
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = finder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[4]
    return success(
      FoundType.CenterTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}