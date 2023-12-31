package com.github.samunohito.mfm.api.parser.block

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.SequentialScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineBeginParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser
import com.github.samunohito.mfm.api.parser.core.fixed.SpaceParser
import com.github.samunohito.mfm.api.utils.next

/**
 * An [IMfmParser] implementation for detecting "quote" syntax.
 * The string enclosed by ">" will be the search result.
 *
 * ### Notes
 * - Perform recursive parsing on quoted content. Not only inline syntax but also block syntax is covered.
 * - Ignore 0-1 spaces after '>'.
 * - Lines of adjacent citations are merged.
 * - Blank lines can be included in multi-line citations.
 * - Empty lines after quotes are ignored.
 */
object QuoteParser : IMfmParser {
  private val quoteParser = SequentialParser(
    NewLineParser.optional(),
    NewLineParser.optional(),
    LineBeginParser,
    QuoteLinesParser,
    NewLineParser.optional(),
    NewLineParser.optional(),
  )

  private object QuoteLinesParser : IMfmParser {
    private val oneLineParser = SequentialParser(
      StringParser(">"),
      SpaceParser.optional(),
      SequentialScanningParser.ofUntil(NewLineParser).optional(),
      NewLineParser.optional(),
    )

    override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
      var latestIndex = startAt
      val lines = mutableListOf<SubstringFoundInfo>()
      while (true) {
        val result = oneLineParser.find(input, latestIndex, context)
        if (!result.success) {
          break
        }

        latestIndex = result.foundInfo.resumeIndex

        // 引用符を省いた本文部分だけを蓄積したい
        lines.add(result.foundInfo[2])
      }

      if (startAt == latestIndex || lines.isEmpty()) {
        // 引用符つきの行が見当たらないときは認識しない
        return failure()
      }

      if (lines.size == 1 && lines[0].contentRange.isEmpty()) {
        // 引用符つきの行が1行のみで、なおかつ引用符のみの行だったときは認識しない
        return failure()
      }

      val range = startAt until latestIndex
      return success(FoundType.Quote, range, range, range.next(), lines)
    }
  }

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = quoteParser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val contents = result.foundInfo[3]
    return success(
      FoundType.Quote,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex,
      contents.nestedInfos
    )
  }
}