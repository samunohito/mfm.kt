package com.github.samunohito.mfm.internal.core

abstract class CharSequenceFinderBase : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    var latestIndex = startAt

    val inputRange = startAt until input.length
    for (i in inputRange) {
      if (!hasNext(input, i)) {
        break
      }

      latestIndex = i
    }

    return if (startAt == latestIndex) {
      // 一回も成功していない場合は失敗扱い
      SubstringFinderResult.ofFailure()
    } else {
      val range = startAt..latestIndex
      SubstringFinderResult.ofSuccess(input, range, range.last + 1)
    }
  }

  protected abstract fun hasNext(text: String, startAt: Int): Boolean
}