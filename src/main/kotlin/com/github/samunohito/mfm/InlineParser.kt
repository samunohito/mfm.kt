package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.IMfmInline

class InlineParser(
  private val callback: Callback = Callback.impl
) : IParser<IMfmInline<*>> {
  private val factories: List<() -> IParser<out IMfmInline<*>>> = listOf(
    { UnicodeEmojiParser() },
    { SmallTagParser() },
    { PlainTagParser() },
    { BoldTagParser() },
    { ItalicTagParser() },
    { StrikeTagParser() },
    { UrlAltParser() },
    { BigParser() },
    { BoldAstaParser() },
    { ItalicAstaParser() },
    { BoldUnderParser() },
    { ItalicUnderParser() },
    { InlineCodeParser() },
    { MathInlineParser() },
    { StrikeWaveParser() },
    { FnParser() },
    { MentionParser() },
    { HashtagParser() },
    { EmojiCodeParser() },
    { LinkParser() },
    { UrlParser() },
  )

  override fun parse(input: String, startAt: Int): ParserResult<IMfmInline<*>> {
    for (factory in factories) {
      val parser = factory.invoke()
      if (callback.needParse(input, startAt, parser)) {
        val result = parser.parse(input, startAt)
        if (result.success) {
          return ParserResult.ofSuccess(result.node, result.range, result.next)
        }
      }
    }

    return ParserResult.ofFailure()
  }

  interface Callback {
    fun needParse(input: String, startAt: Int, parser: IParser<out IMfmInline<*>>): Boolean

    companion object {
      val impl = object : Callback {
        override fun needParse(input: String, startAt: Int, parser: IParser<out IMfmInline<*>>): Boolean {
          return true
        }
      }
    }
  }
}