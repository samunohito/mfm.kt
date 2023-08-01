package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.parser.core.RegexParser
import com.github.samunohito.mfm.api.parser.core.SequentialParser
import com.github.samunohito.mfm.api.parser.core.StringParser
import com.github.samunohito.mfm.api.parser.core.charsequence.AlternateScanningParser
import com.github.samunohito.mfm.api.utils.merge

/**
 * An [IMfmParser] implementation for detecting "URL" syntax.
 * Search results will match patterns where URLs, such as 'http://example.com/' or 'http://さんぷる.com', are enclosed in angle brackets.
 * e.g. <http://example.com/>
 * e.g. <http://さんぷる.com/>
 *
 * ### Notes
 * - Allows https as well as http.
 * - Any characters other than line breaks and spaces can be used in the content.
 */
object UrlAltParser : IMfmParser {
  private val open = StringParser("<")
  private val close = StringParser(">")
  private val schema = RegexParser(Regex("https?://"))
  private val parser = SequentialParser(
    open,
    schema,
    AlternateScanningParser.ofUntil(close),
    close
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    // 開始・終了のブラケットが見つかるまで
    val scanResult = parser.find(input, startAt, context)
    if (!scanResult.success) {
      return failure()
    }

    val schema = scanResult.foundInfo.nestedInfos[1]
    val body = scanResult.foundInfo.nestedInfos[2]
    val urlRange = listOf(schema.contentRange, body.contentRange).merge()
    return success(FoundType.UrlAlt, scanResult.foundInfo.overallRange, urlRange, scanResult.foundInfo.resumeIndex)
  }
}