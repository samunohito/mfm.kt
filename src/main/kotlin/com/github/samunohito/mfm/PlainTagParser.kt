package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.CharSequenceFinderBase
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.SubstringFinderUtils
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmPlain
import com.github.samunohito.mfm.node.MfmText

class PlainTagParser : IParser<MfmPlain> {
  companion object {
    private val open = StringFinder("<plain>")
    private val close = StringFinder("</plain>")

    private object ContentFinder : CharSequenceFinderBase() {
      private val sequenceTerminateFinders = listOf(NewLineFinder.optional(), close)

      override fun hasNext(text: String, startAt: Int): Boolean {
        val result = SubstringFinderUtils.sequential(text, startAt, sequenceTerminateFinders)
        return !result.success
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmPlain> {
    val result = SubstringFinderUtils.sequential(
      input,
      startAt,
      listOf(
        open,
        NewLineFinder.optional(),
        ContentFinder,
        NewLineFinder.optional(),
        close,
      )
    )
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val contents = input.substring(result.nests[2].range)
    val textNode = MfmText(contents)
    return ParserResult.ofSuccess(MfmPlain(listOf(textNode)), input, result.range, result.next)
  }
}