package com.github.samunohito.mfm.finder.core

import com.github.samunohito.mfm.finder.ISubstringFinder
import com.github.samunohito.mfm.finder.ISubstringFinderResult
import com.github.samunohito.mfm.finder.failure
import com.github.samunohito.mfm.finder.success
import com.github.samunohito.mfm.utils.next

abstract class ScanFinderBase : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
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
      failure()
    } else {
      val range = startAt..latestIndex
      success(FoundType.Core, range, range.next())
    }
  }

  protected abstract fun hasNext(text: String, startAt: Int): Boolean
}