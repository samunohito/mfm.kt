@file:Suppress("DuplicatedCode")

package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.AlternateScanFinder
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.internal.core.singleton.SpaceFinder
import com.github.samunohito.mfm.node.MfmBold
import com.github.samunohito.mfm.node.MfmText

class BoldUnderParser : IParser<MfmBold> {
  companion object {
    private val markFinder = StringFinder("__")
    private val italicUnderFinder = SequentialFinder(
      markFinder,
      AlternateScanFinder.ofWhile(RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceFinder),
      markFinder
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmBold> {
    val result = italicUnderFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    val contents = input.substring(result.subResults[1].range)
    val textNode = MfmText(contents)
    return ParserResult.ofSuccess(MfmBold(listOf(textNode)), result.range, result.next)
  }
}