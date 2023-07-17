package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.SequentialScanFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder
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

  override fun parse(input: String, startAt: Int): IParserResult<MfmBlockCode> {
    val result = mathBlockFinder.find(input, startAt)
    if (!result.success) {
      return IParserResult.ofFailure()
    }

    val lang = input.substring(result.subResults[3].range).trim().ifEmpty { null }
    val code = input.substring(result.subResults[5].range)
    return IParserResult.ofSuccess(MfmBlockCode(code, lang), result.range, result.next)
  }
}