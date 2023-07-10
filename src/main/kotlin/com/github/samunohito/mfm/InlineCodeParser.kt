package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.CharSequenceFinderBase
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmInlineCode

class InlineCodeParser : IParser<MfmInlineCode> {
  companion object {
    private val mark = StringFinder("`")
    private val mark2 = StringFinder("´")

    private object FormulaFinder : CharSequenceFinderBase() {
      private val sequenceTerminateFinders = listOf(mark, mark2, NewLineFinder)

      override fun hasNext(text: String, startAt: Int): Boolean {
        val result = SubstringFinderUtils.alternative(text, startAt, sequenceTerminateFinders)
        return !result.success
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmInlineCode> {
    val result = SubstringFinderUtils.sequential(input, startAt, listOf(mark, FormulaFinder, mark))
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val code = input.substring(result.nests[1].range)
    return ParserResult.ofSuccess(MfmInlineCode(code), input, result.range, result.next)
  }
}