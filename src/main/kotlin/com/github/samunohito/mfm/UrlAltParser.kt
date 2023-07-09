package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.node.MfmUrl

class UrlAltParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  companion object {
    private val defaultContext: Context = Context(
      ignoreLinkLabel = false
    )

    private val open = StringFinder("<")
    private val close = StringFinder(">")
    private val schema = RegexFinder(Regex("https?://"))
    private val inequalityBracketWrappers = listOf(
      open,
      schema,
      ScanningFinder(ScanningFinder.Context(scanPeriod = ">")),
      close,
    )

    private class UrlFinder(private val context: Context) : CharSequenceFinderBase() {
      override fun doScanning(text: String, startAt: Int): SubstringFinderResult {
        var latestIndex = startAt
        if (context.ignoreLinkLabel) {
          val scanLinkResult = UrlFinderUtils.scanLink(text, startAt)
          if (scanLinkResult.success) {
            // URLの開始部分まで一気に飛ばす
            latestIndex = scanLinkResult.hrefContents.range.first
          }
        }

        // 開始・終了のブラケットが見つかるまで
        val scanResult = SubstringFinderUtils.sequential(text, latestIndex, inequalityBracketWrappers)
        if (scanResult.success) {
          val schema = scanResult.nests[1]
          val body = scanResult.nests[2]
          val urlRange = schema.range.first..body.range.last
          return SubstringFinderResult.ofSuccess(text, urlRange, scanResult.next)
        }

        return SubstringFinderResult.ofFailure(text, IntRange.EMPTY, scanResult.next)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmUrl> {
    val result = UrlFinder(context).find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val url = input.substring(result.range)
    return ParserResult.ofSuccess(MfmUrl(url, true), input, result.range, result.next)
  }

  data class Context(
    // TODO:もしかしたらいらない説
    var ignoreLinkLabel: Boolean
  )
}