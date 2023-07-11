package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.internal.core.singleton.SpaceFinder
import com.github.samunohito.mfm.node.MfmItalic
import com.github.samunohito.mfm.node.MfmText

class ItalicUnderParser : IParser<MfmItalic> {
  companion object {
    private val mark = StringFinder("_")
    private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)

    private object InnerFinder : ISubstringFinder {
      private val sequenceTerminateFinders = listOf(
        RegexFinder(Regex("[a-z0-9]+", RegexOption.IGNORE_CASE)),
        SpaceFinder
      )

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        val result = SubstringFinderUtils.alternative(input, startAt, sequenceTerminateFinders)
        if (!result.success) {
          return SubstringFinderResult.ofFailure()
        }

        return SubstringFinderResult.ofSuccess(input, startAt..result.range.last, result.next)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmItalic> {
    val result = SubstringFinderUtils.sequential(input, startAt, listOf(mark, InnerFinder, mark))
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    // 直前が英数字だったら認識しない
    val beforeStr = input.substring(0, startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return ParserResult.ofFailure()
    }

    val contents = input.substring(result.nests[1].range)
    val textNode = MfmText(contents)
    return ParserResult.ofSuccess(MfmItalic(listOf(textNode)), input, result.range, result.next)
  }
}