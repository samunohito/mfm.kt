package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.AlternateParser
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser

/**
 * An [IMfmParser] implementation for detecting "strike" syntax.
 * The string enclosed by "~~" will be the search result.
 *
 * ### Notes
 * - Apply [InlineParser] to the content again to recursively detect inline syntax.
 * - Characters other than "~" and newline can be used in the content.
 * - The content cannot be left empty.
 */
object StrikeWaveParser : IMfmParser {
  private val mark = StringParser("~~")
  private val parser = SequentialParser(
    mark,
    InlineParser(AlternateParser(mark, NewLineParser)),
    mark
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = parser.find(input, startAt, context)
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