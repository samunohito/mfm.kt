package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.AlternateParser
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.RegexParser
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

/**
 * An [IMfmParser] implementation for detecting "URL" syntax.
 * Strings that match the pattern "https?://..." are the search results.
 *
 * ### Notes
 * - The content can contain characters matching [.,a-z0-9_/:%#@$&?!~=+-] (case-insensitive).
 * - Parentheses can be used in the content if they come in pairs. Relevant pairs: ()[].
 * - "." or "," cannot be the last character.
 */
object UrlParser : IMfmParser {
  private val regexCommaAndPeriodTail = Regex("[.,]+$")
  private val urlParser = SequentialParser(
    RegexParser(Regex("https?://")),
    UrlBodyParser
  )

  private object UrlBodyParser : IMfmParser {
    private val openRegexBracket = RegexParser(Regex("([(\\[])"))
    private val closeRegexBracket = RegexParser(Regex("([)\\]])"))
    private val nestableParser = AlternateParser(
      // パターンにカッコを含めると、終了カッコのみを誤検出してしまう。
      // …ので、サポートされている種類の開始・終了カッコが揃っている場合は、その中身を再帰的に検索する
      SequentialParser(
        openRegexBracket,
        UrlBodyParser,
        closeRegexBracket,
      ),
      // このパターンに合致する文字が登場するまでを繰り返し検索する
      RegexParser(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+")),
    )

    override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
      var latestIndex = startAt
      val foundInfos = mutableListOf<SubstringFoundInfo>()

      while (true) {
        val result = nestableParser.find(input, latestIndex, context)
        if (!result.success) {
          break
        }

        foundInfos.add(result.foundInfo)
        latestIndex = result.foundInfo.resumeIndex
      }

      if (foundInfos.isEmpty()) {
        return failure()
      }

      val fullRange = startAt until latestIndex
      val resultRange = foundInfos.map { it.contentRange }.merge()
      return success(FoundType.Url, fullRange, resultRange, fullRange.next(), foundInfos)
    }
  }

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = urlParser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val proceedResult = processTrailingPeriodAndComma(input, result)
    if (!proceedResult.success) {
      return failure()
    }

    return success(
      FoundType.Url,
      proceedResult.foundInfo.overallRange,
      proceedResult.foundInfo.contentRange,
      proceedResult.foundInfo.resumeIndex,
      proceedResult.foundInfo.nestedInfos
    )
  }

  /**
   * URLの末尾にピリオドやカンマがある場合は、それを除外した範囲を作成する
   */
  private fun processTrailingPeriodAndComma(
    input: String,
    parserResult: IMfmParserResult
  ): IMfmParserResult {
    val foundInfo = parserResult.foundInfo
    val body = foundInfo.nestedInfos[1]
    val extractUrlBody = input.substring(body.contentRange)
    val matched = regexCommaAndPeriodTail.find(extractUrlBody)
      ?: // 末尾にピリオドやカンマがない場合はそのまま返す
      return parserResult

    if (extractUrlBody.length - matched.value.length <= 0) {
      // スキーマ形式で始まるが、それ以降がカンマとピリオドのみの場合はスキーマ形式として認識しない
      return failure()
    }

    // parserResult の段階でuntilされているので、ここではやらない（多重にやると範囲がおかしくなる）
    val modifyRange = foundInfo.contentRange.first..foundInfo.contentRange.last - matched.value.length
    return success(FoundType.Url, modifyRange, modifyRange, modifyRange.next())
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Scheme(0),
    Body(1),
  }
}