package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.SequentialScanFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.finder.core.singleton.LineBeginFinder
import com.github.samunohito.mfm.finder.core.singleton.LineEndFinder
import com.github.samunohito.mfm.finder.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmMathBlock

class MathBlockParser : IParser<MfmMathBlock> {
  companion object {
    private val open = StringFinder("\\[")
    private val close = StringFinder("\\]")
    private val mathBlockFinder = SequentialFinder(
      NewLineFinder.optional(),
      LineBeginFinder,
      open,
      NewLineFinder.optional(),
      SequentialScanFinder.ofUntil(NewLineFinder.optional(), close),
      NewLineFinder.optional(),
      close,
      LineEndFinder,
      NewLineFinder.optional()
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmMathBlock> {
    val result = mathBlockFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val formula = input.substring(result.subResults[4].range)
    return ParserResult.ofSuccess(MfmMathBlock(formula), result.range, result.next)
  }
}