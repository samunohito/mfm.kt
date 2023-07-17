package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.AlternateFinder
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.utils.merge
import com.github.samunohito.mfm.utils.next

class InlineFinder(
  terminateFinder: ISubstringFinder,
  private val callback: Callback = Callback.impl
) : ISubstringFinder {
  private val terminateFinder = AlternateFinder(terminateFinder, LineEndFinder)
  private val factories: List<() -> ISubstringFinder> = listOf(
    { UnicodeEmojiFinder() },
    { SmallTagFinder() },
    { PlainTagFinder() },
    { BoldTagFinder() },
    { ItalicTagFinder() },
    { StrikeTagFinder() },
    { UrlAltFinder() },
    { BigFinder() },
    { BoldAstaFinder() },
    { ItalicAstaFinder() },
    { BoldUnderFinder() },
    { ItalicUnderFinder() },
    { InlineCodeFinder() },
    { MathInlineFinder() },
    { StrikeWaveFinder() },
    { FnFinder() },
    { MentionFinder() },
    { HashtagFinder() },
    { EmojiCodeFinder() },
    { LinkFinder() },
    { UrlFinder() },
  )

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    var prevIndex = startAt
    var latestIndex = startAt
    val foundInfos = mutableListOf<SubstringFoundInfo>()

    while (!shouldTerminate(input, latestIndex)) {
      val findResult = findWithFactories(input, latestIndex)
      if (findResult.success) {
        if (prevIndex != latestIndex) {
          val range = prevIndex until latestIndex
          foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range.last + 1))
        }
        foundInfos.add(findResult.foundInfo)

        prevIndex = latestIndex
        latestIndex = findResult.foundInfo.next
      } else {
        latestIndex++
      }
    }

    return if (startAt == latestIndex) {
      failure()
    } else {
      if (prevIndex != latestIndex) {
        // ここに来てprevとlatestに差がある場合、リストに登録してないTextが存在するということ
        val range = prevIndex until latestIndex
        foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range.last + 1))
      }

      val range = foundInfos.map { it.range }.merge()
      success(FoundType.Inline, range, range.next())
    }
  }

  private fun shouldTerminate(input: String, latestIndex: Int): Boolean {
    return terminateFinder.find(input, latestIndex).success
  }

  private fun findWithFactories(input: String, latestIndex: Int): ISubstringFinderResult {
    for (factory in factories) {
      val finder = factory.invoke()
      if (callback.needProcess(input, latestIndex, finder)) {
        val result = finder.find(input, latestIndex)
        if (result.success) {
          return result
        }
      }
    }
    return failure()
  }

  interface Callback {
    fun needProcess(input: String, startAt: Int, finder: ISubstringFinder): Boolean

    companion object {
      val impl = object : Callback {
        override fun needProcess(input: String, startAt: Int, finder: ISubstringFinder): Boolean {
          return true
        }
      }
    }
  }
}