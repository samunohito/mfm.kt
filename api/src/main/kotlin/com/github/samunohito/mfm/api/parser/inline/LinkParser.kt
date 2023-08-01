package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.*
import com.github.samunohito.mfm.api.parser.core.fixed.NewLineParser
import com.github.samunohito.mfm.api.utils.next

/**
 * An [IMfmParser] implementation for detecting "link" syntax.
 * Strings that match the pattern "[linkText](https://example.com/)" will be the search results.
 *
 * ### Notes
 * - Apply [InlineParser] to the content again to recursively detect inline syntax.
 * - URLs, links, and mentions are not allowed in the display text (the part enclosed in "[]").
 * - Any character and newline can be used in the content.
 * - The content cannot be left empty.
 */
object LinkParser : IMfmParser {
  private val squareOpen = RegexParser(Regex("\\??\\["))
  private val squareClose = StringParser("]")
  private val roundOpen = StringParser("(")
  private val roundClose = StringParser(")")
  private val linkParser = SequentialParser(
    squareOpen,
    InlineParser(AlternateParser(squareClose, NewLineParser)),
    squareClose,
    roundOpen,
    AlternateParser(UrlAltParser, UrlParser),
    roundClose,
  )
  private val excludeParserClasses = setOf(
    HashtagParser::class.java,
    LinkParser::class.java,
    MentionParser::class.java,
    UrlParser::class.java,
    UrlAltParser::class.java,
  )

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
    // リンク書式のラベル内はハッシュタグ、メンション、リンク、URL、URL(<>付き)を検索しない（テキストとして認識する）
    context.excludeParsers = excludeParserClasses
    val result = linkParser.find(input, startAt, context)
    context.excludeParsers = emptySet()

    if (!result.success) {
      return failure()
    }

    val squareOpen = result.foundInfo.nestedInfos[0]
    val label = result.foundInfo.nestedInfos[1]
    val url = result.foundInfo.nestedInfos[4]

    return success(
      FoundType.Link,
      result.foundInfo.overallRange,
      result.foundInfo.contentRange,
      result.foundInfo.overallRange.next(),
      listOf(squareOpen, label, url)
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    SquareOpen(0),
    Label(1),
    Url(2),
  }
}