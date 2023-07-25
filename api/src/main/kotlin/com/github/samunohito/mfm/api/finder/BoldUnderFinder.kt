package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.SpaceFinder

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

    val contents = result.foundInfo.sub[1]
    return success(FoundType.BoldUnder, contents.range, result.foundInfo.next)
  }
}