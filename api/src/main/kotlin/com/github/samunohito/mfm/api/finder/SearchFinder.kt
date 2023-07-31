package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.api.finder.core.fixed.SpaceFinder
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

/**
 * An [ISubstringFinder] implementation for detecting "search" syntax.
 * Strings that match the pattern "keyword \[検索]" or "keyword \[search]" are the search results.
 *
 * ### Notes
 * - The presence or absence of "[]" is not distinguished.
 * - "search" is case-insensitive
 */
object SearchFinder : ISubstringFinder {
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

    override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
      for (i in startAt until input.length) {
        if (NewLineFinder.find(input, i, context).success) {
          // 改行されていたら検索ボタン形式が破綻するので中断
          return failure()
        }

        val result = searchButtonFinder.find(input, i, context)
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

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = searchFormFinder.find(input, startAt, context)
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