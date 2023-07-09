package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderResult
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils
import com.github.samunohito.mfm.internal.core.singleton.LineBeginFinder
import com.github.samunohito.mfm.internal.core.singleton.LineEndFinder
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.internal.core.singleton.SpaceFinder
import com.github.samunohito.mfm.node.MfmSearch

class SearchParser : IParser<MfmSearch> {
  companion object {
    private val buttonPattern = Regex("\\[?(検索|search)]?", RegexOption.IGNORE_CASE)
    private val buttonFinder = RegexFinder(buttonPattern)


    private object QueryFinder : ISubstringFinder {
      private val searchButtonFinders = listOf(SpaceFinder, buttonFinder, LineEndFinder)

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        var latestIndex = startAt

        for (i in startAt until input.length) {
          if (NewLineFinder.find(input, i).success) {
            // 改行されていたら検索ボタン形式が破綻するので中断
            return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, latestIndex + 1)
          }

          val result = SubstringFinderUtils.sequential(input, latestIndex, searchButtonFinders)
          if (result.success) {
            // 削除ボタンの検出が成功したら、クエリの範囲がわかるのでそれを返す
            val queryRange = startAt until latestIndex
            return SubstringFinderResult.ofSuccess(input, queryRange, queryRange.last + 1)
          }

          latestIndex = i
        }

        // 検索ボタンが見つからなかった
        return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, latestIndex + 1)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmSearch> {
    val text = input.slice(startAt until input.length)

    val chainResult = SubstringFinderUtils.sequential(
      input,
      startAt,
      listOf(
        NewLineFinder.optional(),
        LineBeginFinder,
        QueryFinder,
        SpaceFinder,
        buttonFinder,
        LineEndFinder,
        NewLineFinder.optional()
      )
    )

    if (!chainResult.success) {
      return ParserResult.ofFailure()
    }

    val query = chainResult.nests[2].range
    val space = chainResult.nests[3].range
    val button = chainResult.nests[4].range
    val props = MfmSearch.Props(
      query = text.slice(query),
      content = "${text.slice(query)}${text.slice(space)}${text.slice(button)}"
    )

    return ParserResult.ofSuccess(MfmSearch(props), input, chainResult.range, chainResult.next)
  }
}