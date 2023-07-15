package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.ParserAdapter
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.node.MfmBold
import com.github.samunohito.mfm.node.MfmNest

class BoldAstaParser : IParser<MfmBold> {
  companion object {
    private val mark = StringFinder("**")
    private val boldAstaFinder = SequentialFinder(
      mark,
      ParserAdapter(InlineParser(mark)),
      mark,
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmBold> {
    val result = boldAstaFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val inlineResult = result.subResults[1] as ParserAdapter.Result<*>
    val nest = inlineResult.node as MfmNest<*>
    return ParserResult.ofSuccess(MfmBold.fromNest(nest), result.range, result.next)
  }
}