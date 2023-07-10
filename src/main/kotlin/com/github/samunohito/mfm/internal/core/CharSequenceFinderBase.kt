package com.github.samunohito.mfm.internal.core

abstract class CharSequenceFinderBase : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    var latestResult: SubstringFinderResult = SubstringFinderResult.ofFailure(input, IntRange.EMPTY, startAt)

    val inputRange = startAt until input.length
    for (i in inputRange) {
      val result = doScanning(input, i)
      if (!result.success) {
        break
      }

      latestResult = result
    }

    return if (!latestResult.success) {
      // 一回も成功していない場合は失敗扱い
      SubstringFinderResult.ofFailure(input, IntRange.EMPTY, startAt)
    } else {
      val range = startAt..latestResult.range.last
      SubstringFinderResult.ofSuccess(input, range, range.last + 1)
    }
  }

  protected abstract fun doScanning(text: String, startAt: Int): SubstringFinderResult
}