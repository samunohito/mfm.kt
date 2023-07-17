package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.*
import com.github.samunohito.mfm.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder

class EmojiCodeFinder : ISubstringFinder {
  companion object {
    private val side = RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)).not()
    private val mark = StringFinder(":")
    private val emojiCodeFinder = SequentialFinder(
      AlternateFinder(LineBeginFinder, side),
      mark,
      SequentialFinder(RegexFinder(Regex("[a-z0-9_+-]+", RegexOption.IGNORE_CASE))),
      mark,
      AlternateFinder(LineEndFinder, side)
    )
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = emojiCodeFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val contentsResult = result.foundInfo.sub[2]
    return success(FoundType.EmojiCode, contentsResult.range, contentsResult.next)
  }
}