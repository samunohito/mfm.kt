package com.github.samunohito.mfm.internal.core

object UrlFinderUtils {
  private val schemaFinder = RegexFinder(Regex("https?://"))
  private val wordFinder = RegexFinder(Regex("[.,a-z0-9_/:%#@\\\\$&?!~=+\\-]+"))
  private val openBracket = StringFinder("(")
  private val closeBracket = StringFinder(")")
  private val openSquareBracket = StringFinder("[")
  private val closeSquareBracket = StringFinder("]")

  private val linkLabelFinders = listOf(
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
      val labelFinderResult = linkLabelFinderResult.nests[1]
      val hrefFinderResult = linkLabelFinderResult.nests[4]
      return ScanLinkResult(
        success = true,
        labelContents = labelFinderResult,
        hrefContents = hrefFinderResult,
        next = linkLabelFinderResult.next,
      )
    }

    return ScanLinkResult(
      success = false,
      labelContents = SubstringFinderResult.ofFailure(text, IntRange.EMPTY, -1),
      hrefContents = SubstringFinderResult.ofFailure(text, IntRange.EMPTY, -1),
      next = linkLabelFinderResult.next,
    )
  }

  data class ScanLinkResult(
    val success: Boolean,
    val labelContents: SubstringFinderResult,
    val hrefContents: SubstringFinderResult,
    val next: Int,
  )
}