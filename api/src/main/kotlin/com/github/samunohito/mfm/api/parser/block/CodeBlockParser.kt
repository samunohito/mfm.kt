package com.github.samunohito.mfm.api.parser.block

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.SequentialScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineBeginParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineEndParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser
import com.github.samunohito.mfm.api.utils.next

/**
 * An [IMfmParser] implementation for detecting "code block" syntax.
 * The string enclosed by "```" will be the search result.
 *
 * ### Notes
 * - Nesting of MFM syntax is not possible. All content is interpreted as text.
 * - "```" must be the beginning of the line.
 */
object CodeBlockParser : IMfmParser {
  private val mark = StringParser("```")
  private val codeBlockParser = SequentialParser(
    NewLineParser.optional(),
    LineBeginParser,
    mark,
    SequentialScanningParser.ofUntil(NewLineParser).optional(),
    NewLineParser,
    SequentialScanningParser.ofUntil(NewLineParser, mark, LineEndParser),
    NewLineParser,
    mark,
    LineEndParser,
    NewLineParser.optional()
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = codeBlockParser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val langResult = result.foundInfo.nestedInfos[3]
    val codeResult = result.foundInfo.nestedInfos[5]
    return success(
      FoundType.CodeBlock,
      result.foundInfo.overallRange,
      result.foundInfo.contentRange,
      result.foundInfo.overallRange.next(),
      listOf(
        if (langResult.contentRange.isEmpty()) {
          SubstringFoundInfo.EMPTY
        } else {
          SubstringFoundInfo(
            FoundType.CodeBlock,
            langResult.overallRange,
            langResult.contentRange,
            langResult.contentRange.next()
          )
        },
        SubstringFoundInfo(
          FoundType.CodeBlock,
          codeResult.overallRange,
          codeResult.contentRange,
          codeResult.contentRange.next()
        )
      )
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Lang(0),
    Code(1),
  }
}