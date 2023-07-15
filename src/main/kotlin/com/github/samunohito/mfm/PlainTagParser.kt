package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.SequentialScanFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.finder.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmPlain
import com.github.samunohito.mfm.node.MfmText

class PlainTagParser : IParser<MfmPlain> {
  companion object {
    private val open = StringFinder("<plain>")
    private val close = StringFinder("</plain>")
    private val plainTagFinder = SequentialFinder(
      open,
      NewLineFinder.optional(),
      SequentialScanFinder.ofUntil(NewLineFinder.optional(), close),
      NewLineFinder.optional(),
      close,
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmPlain> {
    val result = plainTagFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val contents = input.substring(result.subResults[2].range)
    val textNode = MfmText(contents)
    return ParserResult.ofSuccess(MfmPlain(listOf(textNode)), result.range, result.next)
  }
}