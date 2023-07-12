package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.SequentialScanFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.singleton.LineBeginFinder
import com.github.samunohito.mfm.internal.core.singleton.LineEndFinder
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.MfmBlockCode

class CodeBlockParser : IParser<MfmBlockCode> {
  companion object {
    private val mark = StringFinder("```")
    private val mathBlockFinder = SequentialFinder(
      NewLineFinder.optional(),
      LineBeginFinder,
      mark,
      SequentialScanFinder.ofUntil(NewLineFinder).optional(),
      NewLineFinder,
      SequentialScanFinder.ofUntil(NewLineFinder, mark, LineEndFinder),
      NewLineFinder,
      mark,
      LineEndFinder,
      NewLineFinder.optional()
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmBlockCode> {
    val result = mathBlockFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val lang = input.substring(result.subResults[3].range).trim().ifEmpty { null }
    val code = input.substring(result.subResults[5].range)
    return ParserResult.ofSuccess(MfmBlockCode(code, lang), input, result.range, result.next)
  }
}