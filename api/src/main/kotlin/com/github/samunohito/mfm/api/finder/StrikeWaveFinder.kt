package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.AlternateFinder
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder

/**
 * An [ISubstringFinder] implementation for detecting "strike" syntax.
 * The string enclosed by "~~" will be the search result.
 *
 * ### Notes
 * - Apply [InlineFinder] to the content again to recursively detect inline syntax.
 * - Characters other than "~" and newline can be used in the content.
 * - The content cannot be left empty.
 */
object StrikeWaveFinder : ISubstringFinder {
  private val mark = StringFinder("~~")
  private val finder = SequentialFinder(
    mark,
    InlineFinder(AlternateFinder(mark, NewLineFinder)),
    mark
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = finder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.StrikeWave,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}