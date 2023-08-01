package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.AlternateScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser

/**
 * An [IMfmParser] implementation for detecting "math inline" syntax.
 * The string enclosed by "\[" and "\]" will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 * - Content cannot be empty.
 * - The content cannot contain line breaks.
 */
object MathInlineParser : IMfmParser {
  private val open = StringParser(word = "\\(")
  private val close = StringParser("\\)")
  private val parser = SequentialParser(
    open,
    AlternateScanningParser.ofUntil(close, NewLineParser),
    close
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = parser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.MathInline,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}