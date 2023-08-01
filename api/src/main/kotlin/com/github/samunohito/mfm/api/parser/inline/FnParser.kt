package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.RegexParser
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

/**
 * An [IMfmParser] implementation for detecting "function" syntax.
 * Strings that match the pattern "$\[functionName.arg1=value,arg2=value contents]" will be the search results.
 *
 * ### Notes
 * - Apply [InlineParser] to the content again to recursively detect inline syntax.
 * - Any character and newline can be used in the content.
 * - The content cannot be left empty.
 */
object FnParser : IMfmParser {
  private val open = StringParser("$[")
  private val close = StringParser("]")
  private val nameParser = RegexParser(Regex("[a-z0-9_]+", RegexOption.IGNORE_CASE))
  private val argsParser = SequentialParser(StringParser("."), ArgsParser)
  private val funcParser = SequentialParser(
    open,
    nameParser,
    argsParser.optional(),
    StringParser(" "),
    InlineParser(close),
    close,
  )

  private object ArgsParser : IMfmParser {
    private val argParser = SequentialParser(
      RegexParser(Regex("[a-z0-9_]+", RegexOption.IGNORE_CASE)),
      SequentialParser(
        StringParser("="),
        RegexParser(Regex("[a-z0-9_.-]+", RegexOption.IGNORE_CASE)),
      ).optional()
    )
    private val argSeparator = StringParser(",")

    override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
      var latestIndex = startAt
      val args = mutableListOf<SubstringFoundInfo>()
      while (true) {
        val argResult = argParser.find(input, latestIndex, context)
        if (argResult.success) {
          args.add(argResult.foundInfo)
          latestIndex = argResult.foundInfo.resumeIndex
        } else {
          val separatorResult = argSeparator.find(input, latestIndex, context)
          if (separatorResult.success) {
            latestIndex = separatorResult.foundInfo.resumeIndex
          } else {
            break
          }
        }
      }

      val fullRange = startAt until latestIndex
      val contentRange = args.map { it.contentRange }.merge()
      return success(FoundType.Fn, fullRange, contentRange, fullRange.next(), args)
    }
  }

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = funcParser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val name = result.foundInfo[1]
    val args = result.foundInfo[2].let {
      if (it.contentRange.isEmpty()) {
        SubstringFoundInfo.EMPTY
      } else {
        // 先頭要素は関数名との区切りに使っているピリオドなので不要
        it[1]
      }
    }
    val content = result.foundInfo[4]

    return success(
      FoundType.Fn,
      result.foundInfo.overallRange,
      result.foundInfo.contentRange,
      result.foundInfo.resumeIndex,
      listOf(name, args, content)
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Name(0),
    Args(1),
    Content(2),
  }
}