package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.*
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.api.utils.next

object LinkFinder : ISubstringFinder {
  private val squareOpen = RegexFinder(Regex("\\??\\["))
  private val squareClose = StringFinder("]")
  private val roundOpen = StringFinder("(")
  private val roundClose = StringFinder(")")
  private val terminateFinder = AlternateFinder(squareClose, NewLineFinder)
  private val linkFinder = SequentialFinder(
    squareOpen,
    InlineFinder(terminateFinder),
    squareClose,
    roundOpen,
    AlternateFinder(UrlAltFinder, UrlFinder),
    roundClose,
  )

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = linkFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val squareOpen = result.foundInfo.sub[0]
    val label = result.foundInfo.sub[1]
    val url = result.foundInfo.sub[4]

    return success(
      FoundType.Link,
      result.foundInfo.fullRange,
      result.foundInfo.contentRange,
      result.foundInfo.fullRange.next(),
      listOf(squareOpen, label, url)
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    SquareOpen(0),
    Label(1),
    Url(2),
  }
}