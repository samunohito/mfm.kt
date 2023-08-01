package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser

/**
 * An [IMfmParser] implementation for detecting "strike" syntax.
 * The string enclosed by <s> tags will be the search result.
 *
 * ### Notes
 * - Apply [InlineParser] to the content again to recursively detect inline syntax.
 * - Any character and newline can be used in the content.
 * - The content cannot be left empty.
 */
object StrikeTagParser : IMfmParser {
  private val open = StringParser("<s>")
  private val close = StringParser("</s>")
  private val parser = SequentialParser(
    open,
    InlineParser(close),
    close
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = parser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.StrikeTag,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}