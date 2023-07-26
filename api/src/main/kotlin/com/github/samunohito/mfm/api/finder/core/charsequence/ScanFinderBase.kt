package com.github.samunohito.mfm.api.finder.core.charsequence

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.failure
import com.github.samunohito.mfm.api.finder.success
import com.github.samunohito.mfm.api.utils.next

abstract class ScanFinderBase : ISubstringFinder {
  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    var latestIndex = startAt

    val inputRange = startAt until input.length
    for (i in inputRange) {
      if (!hasNext(input, i)) {
        break
      }

      latestIndex++
    }

    return if (startAt == latestIndex) {
      // 一回も成功していない場合は失敗扱い
      failure()
    } else {
      val range = startAt until latestIndex
      success(FoundType.Core, range, range.next())
    }
  }

  protected abstract fun hasNext(text: String, startAt: Int): Boolean
}