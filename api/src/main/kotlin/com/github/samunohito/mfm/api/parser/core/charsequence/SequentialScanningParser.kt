package com.github.samunohito.mfm.api.parser.core.charsequence

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.core.utils.ParserUtils

/**
 * Implementation of [ScanningParserBase].
 * [hasNext] becomes false when all [terminates] return success. If they don't return success, it becomes true.
 *
 * @see ScanningParserBase
 */
class SequentialScanningParser private constructor(
  private val terminates: Collection<IMfmParser>
) : ScanningParserBase() {
  override fun hasNext(text: String, startAt: Int, context: IMfmParserContext): Boolean {
    val result = ParserUtils.sequential(text, startAt, terminates, context)
    return !result.success
  }

  companion object {
    fun ofUntil(terminates: Collection<IMfmParser>): SequentialScanningParser {
      return SequentialScanningParser(terminates)
    }

    fun ofUntil(vararg terminates: IMfmParser): SequentialScanningParser {
      return SequentialScanningParser(terminates.toList())
    }
  }
}