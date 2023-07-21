package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.RegexFinder
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.finder.core.fixed.SpaceFinder
import com.github.samunohito.mfm.utils.merge

class SearchFinder : ISubstringFinder {
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
      private val searchButtonFinder = SequentialFinder(
        SpaceFinder, buttonFinder, LineEndFinder
      )

      override fun find(input: String, startAt: Int): ISubstringFinderResult {
        for (i in startAt until input.length) {
          if (NewLineFinder.find(input, i).success) {
            // 改行されていたら検索ボタン形式が破綻するので中断
            return failure()
          }

          val result = searchButtonFinder.find(input, i)
          if (result.success) {
            // 削除ボタンの検出が成功したらクエリの範囲がわかるので、それを返す
            val queryRange = startAt until i
            return success(FoundType.Search, queryRange, queryRange.last + 1)
          }
        }

        // 検索ボタンが見つからなかった
        return failure()
      }
    }
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = searchFormFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val query = result.foundInfo.sub[2]
    val space = result.foundInfo.sub[3]
    val button = result.foundInfo.sub[4]

    val sub = listOf(query, space, button)
    val info = SubstringFoundInfo(
      FoundType.Search,
      sub.map { it.range }.merge(),
      result.foundInfo.next,
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