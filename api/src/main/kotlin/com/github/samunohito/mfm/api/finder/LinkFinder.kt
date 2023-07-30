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
  private val excludeFinderClasses = setOf(
    HashtagFinder::class.java,
    LinkFinder::class.java,
    MentionFinder::class.java,
    UrlFinder::class.java,
    UrlAltFinder::class.java,
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    // リンク書式のラベル内はハッシュタグ、メンション、リンク、URL、URL(<>付き)を検索しない（テキストとして認識する）
    context.excludeFinders = excludeFinderClasses
    val result = linkFinder.find(input, startAt, context)
    context.excludeFinders = emptySet()

    if (!result.success) {
      return failure()
    }

    val squareOpen = result.foundInfo.nestedInfos[0]
    val label = result.foundInfo.nestedInfos[1]
    val url = result.foundInfo.nestedInfos[4]

    return success(
      FoundType.Link,
      result.foundInfo.overallRange,
      result.foundInfo.contentRange,
      result.foundInfo.overallRange.next(),
      listOf(squareOpen, label, url)
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    SquareOpen(0),
    Label(1),
    Url(2),
  }
}