package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.RegexFinder
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.finder.core.fixed.SpaceFinder

class BoldUnderFinder : ISubstringFinder {
  companion object {
    private val mark = StringFinder("__")
    private val boldUnderFinder = SequentialFinder(
      mark,
      AlternateScanFinder.ofWhile(RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceFinder),
      mark
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = boldUnderFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contentsResult = result.foundInfo.sub[1]
    return success(FoundType.Bold, contentsResult.range, result.foundInfo.next)
  }
}