package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.SequentialScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser

/**
 * An [IMfmParser] implementation for detecting "plain tag" syntax.
 * The string enclosed by <plain> tags will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 */
object PlainTagParser : IMfmParser {
  private val open = StringParser("<plain>")
  private val close = StringParser("</plain>")
  private val parser = SequentialParser(
    open,
    NewLineParser.optional(),
    SequentialScanningParser.ofUntil(NewLineParser.optional(), close),
    NewLineParser.optional(),
    close,
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = parser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[2]
    return success(
      FoundType.PlainTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}