package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.CharSequenceFinderBase
import com.github.samunohito.mfm.internal.core.RegexFinder
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

    private object QueryFinder : CharSequenceFinderBase() {
      override fun hasNext(text: String, startAt: Int): Boolean {
        // 検索フォームの書式（スペース、ボタンパターン、行末）が連続している直前までをクエリとしたい
        if (NewLineFinder.find(text, startAt).success) {
          return false
        }

        val buttonFormat = SubstringFinderUtils.sequential(
          text,
          startAt,
          listOf(SpaceFinder, buttonFinder, LineEndFinder)
        )
        if (buttonFormat.success) {
          return false
        }

        return true
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