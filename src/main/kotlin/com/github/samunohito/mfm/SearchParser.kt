package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.finder.core.singleton.LineBeginFinder
import com.github.samunohito.mfm.finder.core.singleton.LineEndFinder
import com.github.samunohito.mfm.finder.core.singleton.NewLineFinder
import com.github.samunohito.mfm.finder.core.singleton.SpaceFinder
import com.github.samunohito.mfm.finder.core.SubstringFinderResult
import com.github.samunohito.mfm.node.MfmSearch

class SearchParser : IParser<MfmSearch> {
  companion object {
    private val buttonFinder = RegexFinder(Regex("\\[?(検索|search)]?", RegexOption.IGNORE_CASE))
    private val searchFormFinder = SequentialFinder(
      NewLineFinder.optional(),
      LineBeginFinder,
      QueryFinder,
      SpaceFinder,
      buttonFinder,
      LineEndFinder,
      NewLineFinder.optional()
    )

    private object QueryFinder : ISubstringFinder {
      private val searchButtonFinders = listOf(SpaceFinder, buttonFinder, LineEndFinder)

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        var latestIndex = startAt

        for (i in startAt until input.length) {
          if (NewLineFinder.find(input, i).success) {
            // 改行されていたら検索ボタン形式が破綻するので中断
            return CoreFinderResult.ofFailure()
          }

          val result = SubstringFinderUtils.sequential(input, latestIndex, searchButtonFinders)
          if (result.success) {
            // 削除ボタンの検出が成功したらクエリの範囲がわかるので、それを返す
            val queryRange = startAt until latestIndex
            return CoreFinderResult.ofSuccess(queryRange, queryRange.last + 1)
          }

          latestIndex = i
        }

        // 検索ボタンが見つからなかった
        return CoreFinderResult.ofFailure()
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmSearch> {
    val text = input.slice(startAt until input.length)

    val result = searchFormFinder.find(text, 0)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val query = result.subResults[2].range
    val space = result.subResults[3].range
    val button = result.subResults[4].range
    val props = MfmSearch.Props(
      query = text.slice(query),
      content = "${text.slice(query)}${text.slice(space)}${text.slice(button)}"
    )

    return ParserResult.ofSuccess(MfmSearch(props), result.range, result.next)
  }
}