package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.AlternateScanFinder
import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmMathInline

class MathInlineParser : IParser<MfmMathInline> {
  companion object {
    private val open = StringFinder("\\(")
    private val close = StringFinder("\\)")
    private val mathInlineFinder = SequentialFinder(
      listOf(
        open,
        AlternateScanFinder.ofUntil(listOf(close, NewLineFinder)),
        close
      )
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmMathInline> {
    val result = mathInlineFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val formula = input.substring(result.subResults[1].range)
    return ParserResult.ofSuccess(MfmMathInline(formula), input, result.range, result.next)
  }
}