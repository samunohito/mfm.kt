package com.github.samunohito.mfm.api.parser.core.charsequence

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.utils.next

/**
 * An implementation of [IMfmParser] that searches the input string until [hasNext] returns false.
 * Starts counting from the search start position and increments the counter if [hasNext] is true. If [hasNext] is false,
 * the range from the search start position to the position of the counter is returned as search results.
 */
abstract class ScanningParserBase : IMfmParser {
  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    var latestIndex = startAt

    val inputRange = startAt until input.length
    for (i in inputRange) {
      if (!hasNext(input, i, context)) {
        break
      }

      latestIndex++
    }

    return if (startAt == latestIndex) {
      // 一回も成功していない場合は失敗扱い
      failure()
    } else {
      val range = startAt until latestIndex
      success(FoundType.Core, range, range, range.next())
    }
  }

  /**
   * Returns true if the search should continue.
   */
  protected abstract fun hasNext(text: String, startAt: Int, context: IMfmParserContext): Boolean
}