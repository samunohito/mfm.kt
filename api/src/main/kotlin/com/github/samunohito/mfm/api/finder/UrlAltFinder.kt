package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.ScanningFinder
import com.github.samunohito.mfm.api.utils.merge

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
      return failure()
    }

    val schema = scanResult.foundInfo.sub[1]
    val body = scanResult.foundInfo.sub[2]
    val urlRange = listOf(schema.contentRange, body.contentRange).merge()
    return success(FoundType.UrlAlt, scanResult.foundInfo.fullRange, urlRange, scanResult.foundInfo.next)
  }
}