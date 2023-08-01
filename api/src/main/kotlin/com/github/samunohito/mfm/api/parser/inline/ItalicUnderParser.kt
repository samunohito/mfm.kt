package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.RegexParser
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.AlternateScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.SpaceParser

/**
 * An [IMfmParser] implementation for detecting "italic" syntax.
 * The string enclosed by "_" will be the search result.
 *
 * ### Notes
 * - Only allow characters matching the regex pattern of [a-z0-9 \t] (case-insensitive)
 * - If the character before the starting symbol (the first "*") matches [a-z0-9] (case-insensitive), it is not recognized as italic formatting.
 * - The content cannot be left empty.
 */
@Suppress("DuplicatedCode")
object ItalicUnderParser : IMfmParser {
  private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
  private val mark = StringParser("_")
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

    // 直前が英数字だったら認識しない
    val beforeStr = input.substring(0, startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[1]
    return success(
      FoundType.ItalicUnder,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}