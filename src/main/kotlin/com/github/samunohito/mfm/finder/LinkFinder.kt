package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.*
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.utils.next

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

    private object InlineFinderCallback : InlineFinder.Callback {
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
    val linkFinderResult = linkFinder.find(input, startAt)
    if (!linkFinderResult.success) {
      return failure()
    }

    val squareOpenResult = linkFinderResult.foundInfo.sub[0]
    val labelResult = linkFinderResult.foundInfo.sub[1]
    val urlResult = linkFinderResult.foundInfo.sub[4]

    return success(
      FoundType.Link,
      linkFinderResult.foundInfo.range,
      linkFinderResult.foundInfo.range.next(),
      listOf(squareOpenResult, labelResult, urlResult)
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    squareOpen(0),
    label(1),
    url(2),
  }
}