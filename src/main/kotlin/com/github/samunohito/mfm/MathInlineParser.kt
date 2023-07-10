package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.CharSequenceFinderBase
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmMathInline

class MathInlineParser : IParser<MfmMathInline> {
  companion object {
    private val open = StringFinder("\\(")
    private val close = StringFinder("\\)")

    private object FormulaFinder : CharSequenceFinderBase() {
      private val sequenceTerminateFinders = listOf(close, NewLineFinder)

      override fun hasNext(text: String, startAt: Int): Boolean {
        val result = SubstringFinderUtils.alternative(text, startAt, sequenceTerminateFinders)
        return !result.success
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmMathInline> {
    val result = SubstringFinderUtils.sequential(input, startAt, listOf(open, FormulaFinder, close))
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val formula = input.substring(result.nests[1].range)
    return ParserResult.ofSuccess(MfmMathInline(formula), input, result.range, result.next)
  }
}