package com.github.samunohito.mfm.api.parser.block

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.SequentialScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineBeginParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineEndParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser

/**
 * An [IMfmParser] implementation for detecting "math block" syntax.
 * The string enclosed by "\\[" and "\\]" will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 * - "\\[" must be the beginning of the line.
 * - "\\]" must be the end of the line.
 * - Leading and trailing spaces and newlines are trimmed.
 */
object MathBlockParser : IMfmParser {
  private val open = StringParser("\\[")
  private val close = StringParser("\\]")
  private val mathBlockParser = SequentialParser(
    NewLineParser.optional(),
    LineBeginParser,
    open,
    NewLineParser.optional(),
    SequentialScanningParser.ofUntil(NewLineParser.optional(), close),
    NewLineParser.optional(),
    close,
    LineEndParser,
    NewLineParser.optional()
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = mathBlockParser.find(input, startAt, context)
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