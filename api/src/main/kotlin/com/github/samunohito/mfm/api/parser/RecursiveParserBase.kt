package com.github.samunohito.mfm.api.parser

import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next


/**
 * Base implementation for detecting nested MFM forms within MFM forms.
 * Parse recursively until [terminateParser] is satisfied.
 * Note that the [IMfmParser]s registered in [IMfmParserContext.excludeParsers],
 * which are passed to [IMfmParser.find], are not used during analysis.
 */
abstract class RecursiveParserBase(
  private val terminateParser: IMfmParser
) : IMfmParser {
  protected abstract val parsers: List<IMfmParser>
  protected abstract val foundType: FoundType

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    var textNodeStartAt = startAt
    var latestIndex = startAt
    val foundInfos = mutableListOf<SubstringFoundInfo>()

    while (!shouldTerminate(input, latestIndex, context)) {
      val findResult = findWithParsers(input, latestIndex, context)
      if (findResult.success) {
        if (textNodeStartAt != latestIndex) {
          // 前回見つかったノードと今回見つかったノードの間にある文字たちをTextノードとして登録する
          val range = textNodeStartAt until latestIndex
          foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range, range.last + 1))
        }
        foundInfos.add(findResult.foundInfo)

        textNodeStartAt = findResult.foundInfo.resumeIndex
        latestIndex = findResult.foundInfo.resumeIndex
      } else {
        latestIndex++
      }
    }

    return if (startAt == latestIndex) {
      failure()
    } else {
      if (textNodeStartAt != latestIndex) {
        // ここに来てprevとlatestに差がある場合、リストに登録してないTextが存在するということ
        val range = textNodeStartAt until latestIndex
        foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range, range.last + 1))
      }

      val fullRange = startAt until latestIndex
      val contentRange = foundInfos.map { it.contentRange }.merge()
      success(foundType, fullRange, contentRange, fullRange.next(), foundInfos)
    }
  }

  private fun shouldTerminate(
    input: String,
    latestIndex: Int,
    context: IMfmParserContext
  ): Boolean {
    if (input.length < latestIndex) {
      return true
    }

    return terminateParser.find(input, latestIndex, context).success
  }

  private fun findWithParsers(
    input: String,
    latestIndex: Int,
    context: IMfmParserContext
  ): IMfmParserResult {
    for (parser in parsers) {
      val result = if (parser::class.java in context.excludeParsers) {
        failure()
      } else {
        parser.find(input, latestIndex, context)
      }

      if (result.success) {
        return result
      }
    }
    return failure()
  }
}