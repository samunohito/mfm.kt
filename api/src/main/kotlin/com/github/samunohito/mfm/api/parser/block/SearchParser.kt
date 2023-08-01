package com.github.samunohito.mfm.api.parser.block

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.RegexParser
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineBeginParser
import com.github.samunohito.mfm.api.parser.core.fixed.LineEndParser
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser
import com.github.samunohito.mfm.api.parser.core.fixed.SpaceParser
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

/**
 * An [IMfmParser] implementation for detecting "search" syntax.
 * Strings that match the pattern "keyword \[検索]" or "keyword \[search]" are the search results.
 *
 * ### Notes
 * - The presence or absence of "[]" is not distinguished.
 * - "search" is case-insensitive
 */
object SearchParser : IMfmParser {
  private val buttonParser = RegexParser(Regex("\\[?(検索|search)]?", RegexOption.IGNORE_CASE))
  private val searchFormParser = SequentialParser(
    NewLineParser.optional(),
    LineBeginParser,
    QueryParser,
    SpaceParser,
    buttonParser,
    LineEndParser,
    NewLineParser.optional()
  )

  private object QueryParser : IMfmParser {
    private val searchButtonParser = SequentialParser(
      SpaceParser, buttonParser, LineEndParser
    )

    override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
      for (i in startAt until input.length) {
        if (NewLineParser.find(input, i, context).success) {
          // 改行されていたら検索ボタン形式が破綻するので中断
          return failure()
        }

        val result = searchButtonParser.find(input, i, context)
        if (result.success) {
          // 削除ボタンの検出が成功したらクエリの範囲がわかるので、それを返す
          val queryRange = startAt until i
          return success(FoundType.Search, queryRange, queryRange, queryRange.next())
        }
      }

      // 検索ボタンが見つからなかった
      return failure()
    }
  }

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val result = searchFormParser.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val query = result.foundInfo.nestedInfos[2]
    val space = result.foundInfo.nestedInfos[3]
    val button = result.foundInfo.nestedInfos[4]

    val sub = listOf(query, space, button)
    val info = SubstringFoundInfo(
      FoundType.Search,
      result.foundInfo.overallRange,
      sub.map { it.contentRange }.merge(),
      result.foundInfo.resumeIndex,
      sub
    )

    return success(info)
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Query(0),
    Space(1),
    Button(2),
  }
}