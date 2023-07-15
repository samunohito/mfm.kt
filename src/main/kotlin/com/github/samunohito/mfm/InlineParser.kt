package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.AlternateFinder
import com.github.samunohito.mfm.internal.core.ISubstringFinder
import com.github.samunohito.mfm.finder.core.singleton.LineEndFinder
import com.github.samunohito.mfm.node.IMfmInline
import com.github.samunohito.mfm.node.MfmNest
import com.github.samunohito.mfm.node.MfmText

class InlineParser(
  terminateFinder: ISubstringFinder,
  private val callback: Callback = Callback.impl
) : IParser<MfmNest<IMfmInline<*>>> {
  private val terminateFinder = AlternateFinder(terminateFinder, LineEndFinder)
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

  override fun parse(input: String, startAt: Int): ParserResult<MfmNest<IMfmInline<*>>> {
    var prevIndex = startAt
    var latestIndex = startAt
    val mfmNodes = mutableListOf<ParserResult<IMfmInline<*>>>()

    while (!shouldTerminate(input, latestIndex)) {
      val parserResult = parseWithFactories(input, latestIndex)
      if (parserResult.success) {
        if (prevIndex != latestIndex) {
          val range = prevIndex until latestIndex
          val text = input.substring(range)
          mfmNodes.add(ParserResult.ofSuccess(MfmText(text), range, range.last + 1))
        }
        mfmNodes.add(ParserResult.ofSuccess(parserResult.node, parserResult.range, parserResult.next))

        prevIndex = latestIndex
        latestIndex = parserResult.next
      } else {
        latestIndex++
      }
    }

    return if (startAt == latestIndex) {
      ParserResult.ofFailure()
    } else {
      val range = startAt until latestIndex
      val nest = createMfmNest(input, range, mfmNodes)
      ParserResult.ofSuccess(nest, range, range.last + 1)
    }
  }

  private fun shouldTerminate(input: String, latestIndex: Int): Boolean {
    return terminateFinder.find(input, latestIndex).success
  }

  private fun parseWithFactories(input: String, latestIndex: Int): ParserResult<IMfmInline<*>> {
    for (factory in factories) {
      val parser = factory.invoke()
      if (callback.needParse(input, latestIndex, parser)) {
        val result = parser.parse(input, latestIndex)
        if (result.success) {
          return ParserResult.ofSuccess(result.node, result.range, result.next)
        }
      }
    }
    return ParserResult.ofFailure()
  }

  private fun createMfmNest(
    input: String,
    range: IntRange,
    mfmNodes: List<ParserResult<IMfmInline<*>>>
  ): MfmNest<IMfmInline<*>> {
    val content = if (mfmNodes.isEmpty()) {
      val text = input.substring(range)
      listOf<IMfmInline<*>>(MfmText(text))
    } else {
      mfmNodes.map { it.node }
    }
    return MfmNest(content)
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