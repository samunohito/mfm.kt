package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder

class InlineFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
) : RecursiveFinderBase(terminateFinder) {
  override val finders: List<ISubstringFinder>
    get() = lazyFinders.value
  override val foundType = FoundType.Inline

  private val lazyFinders = lazy {
    listOf(
      UnicodeEmojiFinder(),
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
      InlineCodeFinder(),
      MathInlineFinder(),
      StrikeWaveFinder(),
      FnFinder(),
      MentionFinder(),
      HashtagFinder(),
      EmojiCodeFinder(),
      LinkFinder(),
      UrlFinder(),
    )
  }
}