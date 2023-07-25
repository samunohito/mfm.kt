package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.*
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.api.utils.next

class LinkFinder : ISubstringFinder {
  companion object {
    private val squareOpen = RegexFinder(Regex("\\??\\["))
    private val squareClose = StringFinder("]")
    private val roundOpen = StringFinder("(")
    private val roundClose = StringFinder(")")
    private val terminateFinder = AlternateFinder(squareClose, NewLineFinder)
    private val linkFinder = SequentialFinder(
      squareOpen,
      InlineFinder(terminateFinder, InlineFinderCallback),
      squareClose,
      roundOpen,
      AlternateFinder(UrlAltFinder(), UrlFinder()),
      roundClose,
    )

    private object InlineFinderCallback : RecursiveFinderBase.Callback {
      override fun needProcess(input: String, startAt: Int, finder: ISubstringFinder): Boolean {
        return when (finder) {
          is HashtagFinder,
          is LinkFinder,
          is MentionFinder,
          is UrlFinder,
          is UrlAltFinder -> {
            false
          }

          else -> {
            true
          }
        }
      }
    }
  }

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
      result.foundInfo.range,
      result.foundInfo.range.next(),
      listOf(squareOpen, label, url)
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    SquareOpen(0),
    Label(1),
    Url(2),
  }
}