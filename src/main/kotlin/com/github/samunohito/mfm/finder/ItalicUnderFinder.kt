package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.RegexFinder
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.finder.core.fixed.SpaceFinder

@Suppress("DuplicatedCode")
class ItalicUnderFinder : ISubstringFinder {
  companion object {
    private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
    private val markFinder = StringFinder("_")
    private val italicUnderFinder = SequentialFinder(
      markFinder,
      AlternateScanFinder.ofWhile(RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceFinder),
      markFinder
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = italicUnderFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    // 直前が英数字だったら認識しない
    val beforeStr = input.substring(0, startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    val contentsResult = result.foundInfo.sub[1]
    return success(FoundType.Italic, contentsResult.range, result.foundInfo.next)
  }
}