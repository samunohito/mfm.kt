package com.github.samunohito.mfm.api.parser.core.charsequence

import com.github.samunohito.mfm.api.parser.IMfmParser
import com.github.samunohito.mfm.api.parser.IMfmParserContext
import com.github.samunohito.mfm.api.parser.IMfmParserResult
import com.github.samunohito.mfm.api.parser.core.utils.ParserUtils

/**
 * Implementation of [ScanningParserBase]. As long as any of the [terminates] returns success, [hasNext] will be true.
 * You can also reverse this behavior (while->until) by setting [isWhile] to false.
 *
 * @see ScanningParserBase
 */
class AlternateScanningParser private constructor(
  private val terminates: Collection<IMfmParser>,
  private val isWhile: Boolean
) : ScanningParserBase() {
  private val handler: (IMfmParserResult) -> Boolean = if (isWhile) {
    { it.success }
  } else {
    { !it.success }
  }

  override fun hasNext(text: String, startAt: Int, context: IMfmParserContext): Boolean {
    val result = ParserUtils.alternate(text, startAt, terminates, context)
    return handler(result)
  }

  companion object {
    fun ofWhile(terminates: Collection<IMfmParser>): AlternateScanningParser {
      return AlternateScanningParser(terminates, true)
    }

    fun ofWhile(vararg terminates: IMfmParser): AlternateScanningParser {
      return AlternateScanningParser(terminates.toList(), true)
    }

    fun ofUntil(terminates: Collection<IMfmParser>): AlternateScanningParser {
      return AlternateScanningParser(terminates, false)
    }

    fun ofUntil(vararg terminates: IMfmParser): AlternateScanningParser {
      return AlternateScanningParser(terminates.toList(), false)
    }
  }
}