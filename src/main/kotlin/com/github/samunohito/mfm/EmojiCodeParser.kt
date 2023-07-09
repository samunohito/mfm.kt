package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.internal.core.singleton.LineBeginFinder
import com.github.samunohito.mfm.internal.core.singleton.LineEndFinder
import com.github.samunohito.mfm.node.MfmEmojiCode

class EmojiCodeParser : IParser<MfmEmojiCode> {
  companion object {
    private val side = RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)).not()
    private val mark = StringFinder(":")
    private val beginEdgeFinder = listOf(LineBeginFinder, side)
    private val endEdgeFinder = listOf(LineEndFinder, side)
    private val codeFinders = listOf(mark, RegexFinder(Regex("[a-z0-9_+-]+", RegexOption.IGNORE_CASE)), mark)

    private object EmojiCodeFinder : ISubstringFinder {

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        val begin = SubstringFinderUtils.alternative(input, startAt, beginEdgeFinder)
        if (!begin.success) {
          return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, begin.next)
        }

        val code = SubstringFinderUtils.sequential(input, begin.next, codeFinders)
        if (!code.success) {
          return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, code.next)
        }

        val end = SubstringFinderUtils.alternative(input, code.next, endEdgeFinder)
        if (!end.success) {
          return SubstringFinderResult.ofFailure(input, IntRange.EMPTY, end.next)
        }

        val name = code.nests[1]
        return SubstringFinderResult.ofSuccess(input, name.range, end.next)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmEmojiCode> {
    val result = EmojiCodeFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val code = input.slice(result.range)
    return ParserResult.ofSuccess(MfmEmojiCode(code), input, result.range, result.next)
  }
}