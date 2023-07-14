package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmLink
import com.github.samunohito.mfm.node.MfmNest

class LinkParser : IParser<MfmLink> {
  companion object {
    private val squareOpen = RegexFinder(Regex("\\??\\["))
    private val squareClose = StringFinder("]")
    private val roundOpen = StringFinder("(")
    private val roundClose = StringFinder(")")
    private val terminateFinder = AlternateFinder(squareClose, NewLineFinder)
    private val linkFinder = SequentialFinder(
      squareOpen,
      ParserAdapter(InlineParser(terminateFinder, NestInlineParserCallback)),
      squareClose,
      roundOpen,
      AlternateFinder(ParserAdapter(UrlAltParser()), ParserAdapter(UrlParser())),
      roundClose,
    )

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
    val url = input.substring(urlResult.range)

    val label = if (labelResult is ParserAdapter.Result<*>) labelResult else error("invalid result type.")
    val nest = if (label.node is MfmNest<*>) label.node as MfmNest<*> else error("invalid node type.")

    return ParserResult.ofSuccess(
      MfmLink.fromNest(isSilent, url, nest),
      linkFinderResult.range,
      linkFinderResult.next
    )
  }
}