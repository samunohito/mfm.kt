package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.AlternateScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser

/**
 * An [IMfmParser] implementation for detecting "inline code" syntax.
 * The string enclosed by "`" will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 * - Any character and newline can be used in the content.
 * - The content cannot be left empty.
 * - The content cannot contain the character "`".
 */
object InlineCodeParser : IMfmParser {
  private val mark = StringParser("`")
  private val mark2 = StringParser("´")
  private val parser = SequentialParser(
    mark,
    AlternateScanningParser.ofUntil(mark, mark2, NewLineParser),
    mark
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = parser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.InlineCode,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}