package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.ScanningFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils
import com.github.samunohito.mfm.node.MfmUrl

class UrlAltParser(private val context: Context = defaultContext) : IParser<MfmUrl> {
  companion object {
    private val defaultContext: Context = Context(
      disabled = false
    )

    private val open = StringFinder("<")
    private val close = StringFinder(">")
    private val schema = RegexFinder(Regex("https?://"))
    private val inequalityBracketWrappers = listOf(
      open,
      schema,
      ScanningFinder(">"),
      close,
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmUrl> {
    if (context.disabled) {
      return ParserResult.ofFailure()
    }

    // 開始・終了のブラケットが見つかるまで
    val scanResult = SubstringFinderUtils.sequential(input, startAt, inequalityBracketWrappers)
    if (!scanResult.success) {
      return ParserResult.ofFailure()
    }

    val schema = scanResult.nests[1]
    val body = scanResult.nests[2]
    val urlRange = schema.range.first..body.range.last
    val url = input.substring(urlRange)
    return ParserResult.ofSuccess(MfmUrl(url, true), input, urlRange, scanResult.next)
  }

  data class Context(
    var disabled: Boolean
  )
}