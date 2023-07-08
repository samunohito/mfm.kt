package com.github.samunohito.mfm.internal.core2.parser

import com.github.samunohito.mfm.internal.core.type.node.MfmSearch
import com.github.samunohito.mfm.internal.core2.parser.core.RegexParser
import com.github.samunohito.mfm.internal.core2.parser.core.singleton.LineBeginParser
import com.github.samunohito.mfm.internal.core2.parser.core.singleton.LineEndParser
import com.github.samunohito.mfm.internal.core2.parser.core.singleton.NewLineParser
import com.github.samunohito.mfm.internal.core2.parser.core.singleton.SpaceParser

class SearchParser {
  companion object {
    private val buttonPattern = Regex("\\[?(検索|search)]?", RegexOption.IGNORE_CASE)
    private val buttonParser = RegexParser(buttonPattern)


    private object QueryParser : IParser2<String> {
      override fun parse(input: String, startAt: Int): Result2<String> {
        val text = input.slice(startAt..input.length)
        val queryRangeLast = text.indices.firstOrNull { !hasNextQuery(text, it) }
          ?: return Result2.ofFailure()

        val range = (startAt..queryRangeLast)
        return Result2.ofSuccess(range, text.slice(range))
      }

      private fun hasNextQuery(text: String, startAt: Int): Boolean {
        // 検索フォームの書式（スペース、ボタンパターン、行末）が連続している直前までをクエリとしたい
        if (NewLineParser.parse(text, startAt).success) {
          return false
        }

        if (ParserUtils.sequential(text, startAt, listOf(SpaceParser, buttonParser, LineEndParser)).success) {
          return false
        }

        return true
      }
    }
  }

  fun parse(input: String, startAt: Int = 0): Result2<MfmSearch> {
    val text = input.slice(startAt..input.length)

    val parserChain = listOf(
      NewLineParser.optional(),
      LineBeginParser.nullable(),
      QueryParser.nullable(),
      SpaceParser.nullable(),
      buttonParser.nullable(),
      LineEndParser.nullable(),
      NewLineParser.optional()
    )

    val chainResult = ParserUtils.sequential(input, startAt, parserChain)
  }
}