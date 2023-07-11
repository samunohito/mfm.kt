package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.AlternateScanFinder
import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmInlineCode

class InlineCodeParser : IParser<MfmInlineCode> {
  companion object {
    private val mark = StringFinder("`")
    private val mark2 = StringFinder("´")
    private val inlineCodeFinder = SequentialFinder(
      listOf(
        mark,
        AlternateScanFinder.ofUntil(listOf(mark, mark2, NewLineFinder)),
        mark
      )
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmInlineCode> {
    val result = inlineCodeFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val code = input.substring(result.subResults[1].range)
    return ParserResult.ofSuccess(MfmInlineCode(code), input, result.range, result.next)
  }
}