package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.AlternateFinder
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.singleton.LineBeginFinder
import com.github.samunohito.mfm.internal.core.singleton.LineEndFinder
import com.github.samunohito.mfm.node.MfmEmojiCode

class EmojiCodeParser : IParser<MfmEmojiCode> {
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

  override fun parse(input: String, startAt: Int): ParserResult<MfmEmojiCode> {
    val result = emojiCodeFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val code = input.slice(result.subResults[2].range)
    return ParserResult.ofSuccess(MfmEmojiCode(code), result.range, result.next)
  }
}