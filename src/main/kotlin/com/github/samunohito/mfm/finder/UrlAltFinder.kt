package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.*
import com.github.samunohito.mfm.utils.merge

class UrlAltFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder("<")
    private val close = StringFinder(">")
    private val schema = RegexFinder(Regex("https?://"))
    private val urlFinder = SequentialFinder(open, schema, ScanningFinder(">"), close)
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    // 開始・終了のブラケットが見つかるまで
    val scanResult = urlFinder.find(input, startAt)
    if (!scanResult.success) {
      return SubstringFinderResult.ofFailure()
    }

    val schema = scanResult.foundInfo.sub[1]
    val body = scanResult.foundInfo.sub[2]
    val urlRange = listOf(schema.range, body.range).merge()
    return SubstringFinderResult.ofSuccess(FoundType.UrlAlt, urlRange, scanResult.foundInfo.next)
  }
}