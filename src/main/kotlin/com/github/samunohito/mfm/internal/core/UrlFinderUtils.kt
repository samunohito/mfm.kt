package com.github.samunohito.mfm.internal.core

object UrlFinderUtils {
  private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))
  private val openBracket = StringFinder("(")
  private val closeBracket = StringFinder(")")
  private val silentMark = StringFinder("?")
  private val openSquareBracket = StringFinder("[")
  private val closeSquareBracket = StringFinder("]")

  private val linkLabelFinders = listOf(
    silentMark.optional(),
    openSquareBracket,
    wordFinder,
    closeSquareBracket,
    openBracket,
    wordFinder,
    closeBracket,
  )

  fun scanLink(text: String, startAt: Int): ScanLinkResult {
    val linkLabelFinderResult = SubstringFinderUtils.sequential(text, startAt, linkLabelFinders)
    if (linkLabelFinderResult.success) {
      // リンク形式として完成している場合はリンクラベル部分を無視してhref部分をチェックしたい
      val silentResult = linkLabelFinderResult.nests[0]
      val labelFinderResult = linkLabelFinderResult.nests[2]
      val hrefFinderResult = linkLabelFinderResult.nests[5]
      return ScanLinkResult(
        success = true,
        // かっこ前に?がある場合、rangeが空ではなくなる（optionalの都合上、successは常にtrueになるので使えない）
        silent = !silentResult.range.isEmpty(),
        labelContents = labelFinderResult,
        hrefContents = hrefFinderResult,
        next = linkLabelFinderResult.next,
      )
    }

    return ScanLinkResult(
      success = false,
      silent = false,
      labelContents = SubstringFinderResult.ofFailure(),
      hrefContents = SubstringFinderResult.ofFailure(),
      next = linkLabelFinderResult.next,
    )
  }

  data class ScanLinkResult(
    val success: Boolean,
    val silent: Boolean,
    val labelContents: SubstringFinderResult,
    val hrefContents: SubstringFinderResult,
    val next: Int,
  )
}