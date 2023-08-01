package com.github.samunohito.mfm.api.parser.core.utils

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.utils.next

object ParserUtils {
  fun sequential(
    input: String,
    startAt: Int,
    parsers: Collection<IMfmParser>,
    context: IMfmParserContext
  ): IMfmParserResult {
    var latestIndex = startAt
    val results = mutableListOf<SubstringFoundInfo>()

    for (parser in parsers) {
      val result = parser.find(input, latestIndex, context)
      if (!result.success) {
        return failure()
      }

      latestIndex = result.foundInfo.resumeIndex
      results.add(result.foundInfo)
    }

    val resultRange = startAt until latestIndex
    return success(
      FoundType.Core,
      resultRange,
      resultRange,
      resultRange.next(),
      results
    )
  }

  fun alternate(
    input: String,
    startAt: Int,
    parsers: Collection<IMfmParser>,
    context: IMfmParserContext
  ): IMfmParserResult {
    for (parser in parsers) {
      val result = parser.find(input, startAt, context)
      if (result.success) {
        return result
      }
    }

    return failure()
  }
}