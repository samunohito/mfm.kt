package com.github.samunohito.mfm.api.parser.core

import com.github.samunohito.mfm.api.parser.*

/**
 * An implementation of [IMfmParser] that searches for a regular expression in a given string.
 *
 * @param regex A regular expression pattern to search for. The search will succeed only if there is a string that matches this pattern at the search start position.
 */
class RegexParser(regex: Regex) : IMfmParser {
  // 行頭フラグをつけておかないと、検索開始位置よりも後ろにある文字列に反応してしまって正しい検索結果にならない
  private val _regex = Regex("^${regex.pattern}", regex.options)

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    val text = input.substring(startAt)
    val result = _regex.find(text)
    return if (result == null) {
      failure()
    } else {
      val resultRange = startAt until (startAt + result.value.length)
      success(FoundType.Core, resultRange, resultRange, resultRange.last + 1)
    }
  }
}