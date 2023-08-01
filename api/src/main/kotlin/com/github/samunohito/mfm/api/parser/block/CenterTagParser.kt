package com.github.samunohito.mfm.api.parser.block

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineBeginParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineEndParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser

/**
 * An [IMfmParser] implementation for detecting "center" syntax.
 * The string enclosed by <center> tags will be the search result.
 *
 * ### Notes
 * - Apply [InlineParser] to the content again to recursively detect inline syntax.
 * - <center> must be the beginning of the line.
 * - </center> must be the end of the line.
 */
object CenterTagParser : IMfmParser {
  private val open = StringParser("<center>")
  private val close = StringParser("</center>")
  private val parser = SequentialParser(
    NewLineParser.optional(),
    LineBeginParser,
    open,
    NewLineParser.optional(),
    InlineParser(SequentialParser(NewLineParser.optional(), close)),
    NewLineParser.optional(),
    close,
    LineEndParser,
    NewLineParser.optional(),
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = parser.find(input, startAt, context)
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