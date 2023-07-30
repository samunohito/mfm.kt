package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.RegexFinder
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineEndFinder

/**
 * An [ISubstringFinder] implementation for detecting "emoji code" syntax.
 * The string enclosed by ":" will be the search result.
 *
 * ### Notes
 * - Only allow characters matching the regex pattern of [a-z0-9_+-] (case insensitive)
 * - The Content cannot be left empty.
 */
object EmojiCodeFinder : ISubstringFinder {
  private val regexSide = Regex("[a-z0-9]", RegexOption.IGNORE_CASE)
  private val mark = StringFinder(":")
  private val emojiCodeFinder = SequentialFinder(
    LineBeginFinder.optional(),
    mark,
    SequentialFinder(RegexFinder(Regex("[a-z0-9_+-]+", RegexOption.IGNORE_CASE))),
    mark,
    LineEndFinder.optional(),
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = emojiCodeFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    if (!validateBeforeSide(input, startAt, result.foundInfo) && !validateAfterSide(input, result.foundInfo)) {
      // 両脇がコロンだった場合は絵文字コードとして認識しない
      return failure()
    }

    val contents = result.foundInfo.nestedInfos[2]
    return success(
      FoundType.EmojiCode,
      result.foundInfo.overallRange,
      contents.contentRange,
      result.foundInfo.resumeIndex
    )
  }

  private fun validateBeforeSide(
    input: String,
    startAt: Int,
    foundInfo: SubstringFoundInfo
  ): Boolean {
    // コロンの直前が半角英数の場合は絵文字コードとして認識しない
    if (startAt >= 1) {
      val idx = foundInfo.contentRange.first
      val beforeStr = input.substring(idx - 1, idx)
      if (regexSide.containsMatchIn(beforeStr)) {
        return false
      }
    }

    return true
  }

  private fun validateAfterSide(
    input: String,
    foundInfo: SubstringFoundInfo
  ): Boolean {
    // コロンの直後が半角英数の場合は絵文字コードとして認識しない
    if ((input.length - 1) >= foundInfo.contentRange.last + 1) {
      // 範囲を外れた次の1文字目を取りたいので+1している
      val idx = foundInfo.contentRange.last + 1
      val afterStr = input.substring(idx, idx + 1)
      if (regexSide.containsMatchIn(afterStr)) {
        return false
      }
    }

    return true
  }

}