@file:Suppress("DuplicatedCode")

package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.core.AlternateScanFinder
import com.github.samunohito.mfm.internal.core.RegexFinder
import com.github.samunohito.mfm.internal.core.SequentialFinder
import com.github.samunohito.mfm.internal.core.StringFinder
import com.github.samunohito.mfm.finder.core.singleton.SpaceFinder
import com.github.samunohito.mfm.node.MfmItalic
import com.github.samunohito.mfm.node.MfmText

class ItalicUnderParser : IParser<MfmItalic> {
  companion object {
    private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
    private val markFinder = StringFinder("_")
    private val italicUnderFinder = SequentialFinder(
      markFinder,
      AlternateScanFinder.ofWhile(RegexFinder(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)), SpaceFinder),
      markFinder
    )
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmItalic> {
    val result = italicUnderFinder.find(input, startAt)
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    // 直前が英数字だったら認識しない
    val beforeStr = input.substring(0, startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return ParserResult.ofFailure()
    }

    val contents = input.substring(result.subResults[1].range)
    val textNode = MfmText(contents)
    return ParserResult.ofSuccess(MfmItalic(listOf(textNode)), result.range, result.next)
  }
}