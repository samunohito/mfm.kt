package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.CharSequenceFinderBase
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils
import com.github.samunohito.mfm.node.MfmUrl

class UrlAltParser : IParser<MfmUrl> {
  companion object {
    private val open = StringFinder("<")
    private val close = StringFinder(">")
    private val protocol = RegexFinder(Regex("https?://"))

    private object UrlFinder : CharSequenceFinderBase() {
      override fun hasNext(text: String, startAt: Int): Boolean {
        // 開始・終了のブラケットが見つかるまで
        if (SubstringFinderUtils.alternative(text, startAt, listOf(open, close)).success) {
          return false
        }

        return true
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmUrl> {
    val chainResult = SubstringFinderUtils.sequential(
      input,
      startAt,
      listOf(
        open,
        protocol,
        UrlFinder,
        close
      )
    )

    if (!chainResult.success) {
      return ParserResult.ofFailure()
    }

    val protocol = input.substring(chainResult.nests[1].range)
    val body = input.substring(chainResult.nests[2].range)
    val urlRange = chainResult.nests[1].range.first..chainResult.nests[2].range.last
    return ParserResult.ofSuccess(MfmUrl(protocol + body, true), input, urlRange, chainResult.next)
  }
}