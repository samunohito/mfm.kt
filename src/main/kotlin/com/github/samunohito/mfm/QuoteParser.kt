package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.FullFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.fixed.CharSequenceEndFinder
import com.github.samunohito.mfm.node.MfmQuote

class QuoteParser : SimpleParserBase<MfmQuote>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Quote)

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmQuote> {
    val contentLines = foundInfo.sub.map { input.substring(it.range) }
    if (contentLines.isEmpty() || (contentLines.size == 1 && contentLines[0].isEmpty())) {
      // disallow empty content if single line
      return failure()
    }

    // parse inner content
    val contentText = contentLines.joinToString("\n")
    val contentFindResult = FullFinder(CharSequenceEndFinder).find(contentText)
    if (!contentFindResult.success) {
      return failure()
    }

    val result = FullParser().parse(contentText, contentFindResult.foundInfo)
    if (!result.success) {
      return failure()
    }

    return success(MfmQuote(result.node.children), foundInfo)
  }
}