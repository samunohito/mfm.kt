package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmLink
import com.github.samunohito.mfm.node.MfmText

class LinkParser : IParser<MfmLink> {
  companion object {
    private val squareOpen = RegexFinder(Regex("\\??\\["))
    private val squareClose = RegexFinder(Regex("\\]"))
    private val roundOpen = RegexFinder(Regex("\\("))
    private val roundClose = RegexFinder(Regex("\\)"))

    private val linkFinder = SequentialFinder(
      squareOpen,
      NestInlineFinder,
      squareClose,
      roundOpen,
      AlternateFinder(ParserWrapper(UrlAltParser()), ParserWrapper(UrlParser())),
      roundClose,
    )

    private object NestInlineFinder : ISubstringFinder {
      private val terminateFinder = AlternateFinder(squareClose, NewLineFinder)

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        // ネストしたパーサーがすべて失敗した文字はMfmTextとして扱いたいので、失敗した範囲を覚えておくための変数
        var latestIndex = startAt

        while (true) {
          val terminateResult = terminateFinder.find(input, latestIndex)
          if (terminateResult.success) {
            break
          }

          // ネストしたパーサー内で成功していたらその結果を活かす
          val nestResult = InlineParser(NestInlineParserCallback).parse(input, latestIndex)
          if (nestResult.success) {
            return SubstringFinderResult.ofSuccess(nestResult.range, nestResult.next)
          }

          latestIndex++
        }

        val range = startAt until latestIndex
        return SubstringFinderResult.ofSuccess(range, range.last + 1)
      }
    }

    private object NestInlineParserCallback : InlineParser.Callback {
      override fun needParse(input: String, startAt: Int, parser: IParser<out IMfmInline<*>>): Boolean {
        return when (parser) {
          is HashtagParser,
          is LinkParser,
          is MentionParser,
          is UrlParser,
          is UrlAltParser -> {
            false
          }

          else -> {
            true
          }
        }
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmLink> {
    val linkFinderResult = linkFinder.find(input, startAt)
    if (!linkFinderResult.success) {
      return ParserResult.ofFailure()
    }

    val squareOpenResult = linkFinderResult.subResults[0]
    val labelResult = linkFinderResult.subResults[1]
    val urlResult = linkFinderResult.subResults[4]

    val isSilent = input.substring(squareOpenResult.range) == "?["
    val label = input.substring(labelResult.range)
    val url = input.substring(urlResult.range)

    return ParserResult.ofSuccess(
      MfmLink(isSilent, url, listOf(MfmText(label))),
      linkFinderResult.range,
      linkFinderResult.next
    )
  }
}