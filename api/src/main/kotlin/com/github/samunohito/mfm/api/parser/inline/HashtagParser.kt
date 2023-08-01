package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.*
import com.github.samunohito.mfm.api.parser.core.charsequence.AlternateScanningParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser
import com.github.samunohito.mfm.api.parser.core.fixed.SpaceParser
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

/**
 * An [IMfmParser] implementation for detecting "hashtag" syntax.
 * Strings starting with "#" will be search results.
 *
 * ### Notes
 * - The content cannot be left empty.
 * - The content cannot contain half-width spaces, full-width spaces, newlines, or tab characters.
 * - The content cannot include . , ! ? ' " # : / 【 】 < > 【 】 ( ) 「 」 （ ）.
 * - Parentheses can only be included in the content when they come in pairs. Relevant pairs: () [] 「」 （）.
 * - Recognize as a hashtag if the character before # does not match [a-z0-9](case-insensitive).
 * - If the content is only numbers, it will not be recognized as a hashtag.
 */
object HashtagParser : IMfmParser {
  private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
  private val regexNumericOnly = Regex("^[0-9]+$")
  private val mark = StringParser("#")
  private val hashtagParser = SequentialParser(mark, HashtagBodyParser)

  private object HashtagBodyParser : IMfmParser {
    private val openRegexBracket = RegexParser(Regex("[(\\[「（]"))
    private val closeRegexBracket = RegexParser(Regex("[)\\]」）]"))
    private val nestableParser = AlternateParser(
      // パターンにカッコを含めると、終了カッコのみを誤検出してしまう。
      // …ので、サポートされている種類の開始・終了カッコが揃っている場合は、その中身を再帰的に検索する
      SequentialParser(
        openRegexBracket,
        HashtagBodyParser,
        closeRegexBracket,
      ),
      // このパターンに合致する文字が登場するまでを繰り返し検索する
      AlternateScanningParser.ofUntil(
        SpaceParser,
        NewLineParser,
        RegexParser(Regex("[ \\u3000\\t.,!?'\"#:/\\[\\]【】()「」（）<>]")),
      )
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
      val contentRange = foundInfos.map { it.contentRange }.merge()
      return success(FoundType.Hashtag, fullRange, contentRange, fullRange.next(), foundInfos)
    }
  }

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = hashtagParser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    // ハッシュタグの直前が英数字の場合はハッシュタグとして認識しない
    val beforeStr = input.substring(0 until startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    // 検出された文字が数値のみの場合はハッシュタグではない
    val hashtagNameResult = result.foundInfo.nestedInfos[1]
    val hashtagName = input.substring(hashtagNameResult.contentRange)
    if (regexNumericOnly.containsMatchIn(hashtagName)) {
      return failure()
    }

    return success(
      FoundType.Hashtag,
      result.foundInfo.overallRange,
      hashtagNameResult.contentRange,
      result.foundInfo.resumeIndex
    )
  }
}