package com.github.samunohito.mfm.internal.core

class RegexFinder(regex: Regex) : ISubstringFinder {
  private val _regex = Regex("^(?:${regex.pattern})", regex.options)

  override fun find(input: String, startAt: Int): SubstringFinderResult {
    val text = input.substring(startAt)
    val result = _regex.find(text)
    return if (result == null) {
      SubstringFinderResult.ofFailure(input, IntRange.EMPTY, startAt)
    } else {
      val resultRange = startAt until (startAt + result.value.length)
      SubstringFinderResult.ofSuccess(input, resultRange, resultRange.last + 1)
    }
  }
}