package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder

class FullFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
) : RecursiveFinderBase(terminateFinder) {
  override val finders: List<ISubstringFinder>
    get() = lazyFinders.value
  override val foundType = FoundType.Full

  private val lazyFinders = lazy {
    listOf(
      UnicodeEmojiFinder(),
      CenterTagFinder(),
      SmallTagFinder(),
      PlainTagFinder(),
      BoldTagFinder(),
      ItalicTagFinder(),
      StrikeTagFinder(),
      UrlAltFinder(),
      BigFinder(),
      BoldAstaFinder(),
      ItalicAstaFinder(),
      BoldUnderFinder(),
      ItalicUnderFinder(),
      CodeBlockFinder(),
      InlineCodeFinder(),
      QuoteFinder(),
      MathBlockFinder(),
      MathInlineFinder(),
      StrikeWaveFinder(),
      FnFinder(),
      MentionFinder(),
      HashtagFinder(),
      EmojiCodeFinder(),
      LinkFinder(),
      UrlFinder(),
      SearchFinder(),
    )
  }
}