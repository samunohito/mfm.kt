package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.ScanningFinder
import com.github.samunohito.mfm.api.utils.merge

object UrlAltFinder : ISubstringFinder {
  private val open = StringFinder("<")
  private val close = StringFinder(">")
  private val schema = RegexFinder(Regex("https?://"))
  private val urlFinder = SequentialFinder(open, schema, ScanningFinder(">"), close)

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    // 開始・終了のブラケットが見つかるまで
    val scanResult = urlFinder.find(input, startAt, context)
    if (!scanResult.success) {
      return failure()
    }

    val schema = scanResult.foundInfo.nestedInfos[1]
    val body = scanResult.foundInfo.nestedInfos[2]
    val urlRange = listOf(schema.contentRange, body.contentRange).merge()
    return success(FoundType.UrlAlt, scanResult.foundInfo.overallRange, urlRange, scanResult.foundInfo.resumeIndex)
  }
}