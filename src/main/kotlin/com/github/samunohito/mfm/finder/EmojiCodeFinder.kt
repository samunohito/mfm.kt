package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.RegexFinder
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder

class EmojiCodeFinder : ISubstringFinder {
  companion object {
    private val regexSide = Regex("[a-z0-9]", RegexOption.IGNORE_CASE)
    private val mark = StringFinder(":")
    private val emojiCodeFinder = SequentialFinder(
      LineBeginFinder.optional(),
      mark,
      SequentialFinder(RegexFinder(Regex("[a-z0-9_+-]+", RegexOption.IGNORE_CASE))),
      mark,
      LineEndFinder.optional(),
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = emojiCodeFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    if (!validateBeforeSide(input, startAt, result.foundInfo) && !validateAfterSide(input, startAt, result.foundInfo)) {
      // 両脇がコロンだった場合は絵文字コードとして認識しない
      return failure()
    }

    val contents = result.foundInfo.sub[2]
    return success(FoundType.EmojiCode, contents.range, result.foundInfo.next)
  }

  private fun validateBeforeSide(input: String, startAt: Int, foundInfo: SubstringFoundInfo): Boolean {
    // コロンの直前が半角英数の場合は絵文字コードとして認識しない
    if (startAt >= 1) {
      val idx = foundInfo.range.first
      val beforeStr = input.substring(idx - 1, idx)
      if (regexSide.containsMatchIn(beforeStr)) {
        return false
      }
    }

    return true
  }

  private fun validateAfterSide(input: String, startAt: Int, foundInfo: SubstringFoundInfo): Boolean {
    // コロンの直後が半角英数の場合は絵文字コードとして認識しない
    if ((input.length - 1) >= foundInfo.range.last + 1) {
      // 範囲を外れた次の1文字目を取りたいので+1している
      val idx = foundInfo.range.last + 1
      val afterStr = input.substring(idx, idx + 1)
      if (regexSide.containsMatchIn(afterStr)) {
        return false
      }
    }

    return true
  }

}