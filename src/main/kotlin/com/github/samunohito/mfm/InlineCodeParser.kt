package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.node.MfmInlineCode

class InlineCodeParser : IParser<MfmInlineCode> {
  companion object {
    private val mark = StringFinder("`")
    private val mark2 = StringFinder("´")
    private val inlineCodeFinder = SequentialFinder(
      mark,
      AlternateScanFinder.ofUntil(mark, mark2, NewLineFinder),
      mark
    )
  }

  override fun parse(input: String, startAt: Int): IParserResult<MfmInlineCode> {
    val result = inlineCodeFinder.find(input, startAt)
    if (!result.success) {
      return IParserResult.ofFailure()
    }

    val code = input.substring(result.subResults[1].range)
    return IParserResult.ofSuccess(MfmInlineCode(code), result.range, result.next)
  }
}