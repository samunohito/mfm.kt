package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder

/**
 * An [ISubstringFinder] implementation for detecting inline syntax.
 */
class InlineFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
) : RecursiveFinderBase(terminateFinder) {
  override val finders: List<ISubstringFinder>
    get() = _finders
  override val foundType = FoundType.Inline

  companion object {
    private val _finders = listOf(
      UnicodeEmojiFinder,
      SmallTagFinder,
      PlainTagFinder,
      BoldTagFinder,
      ItalicTagFinder,
      StrikeTagFinder,
      UrlAltFinder,
      BigFinder,
      BoldAstaFinder,
      ItalicAstaFinder,
      BoldUnderFinder,
      ItalicUnderFinder,
      InlineCodeFinder,
      MathInlineFinder,
      StrikeWaveFinder,
      FnFinder,
      MentionFinder,
      HashtagFinder,
      EmojiCodeFinder,
      LinkFinder,
      UrlFinder,
    )
  }
}