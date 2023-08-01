package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.RegexParser
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.AlternateScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.SpaceParser

/**
 * An [IMfmParser] implementation for detecting "bold" syntax.
 * The string enclosed by "__" will be the search result.
 *
 * ### Notes
 * - Only allow characters matching the regex pattern of [a-z0-9 \t] (case-insensitive)
 * - The Content cannot be left empty.
 */
object BoldUnderParser : IMfmParser {
  private val mark = StringParser("__")
  private val parser = SequentialParser(
    mark,
    AlternateScanningParser.ofWhile(RegexParser(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceParser),
    mark
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = parser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.BoldUnder,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}