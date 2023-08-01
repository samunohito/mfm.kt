package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.block.*
import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder
import com.github.samunohito.mfm.api.finder.inline.*

/**
 * An [ISubstringFinder] implementation for detecting all syntax.
 */
class FullFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
) : RecursiveFinderBase(terminateFinder) {
  override val finders: List<ISubstringFinder>
    get() = _finders
  override val foundType = FoundType.Full

  companion object {
    private val _finders = listOf(
      UnicodeEmojiFinder,
      CenterTagFinder,
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
      CodeBlockFinder,
      InlineCodeFinder,
      QuoteFinder,
      MathBlockFinder,
      MathInlineFinder,
      StrikeWaveFinder,
      FnFinder,
      MentionFinder,
      HashtagFinder,
      EmojiCodeFinder,
      LinkFinder,
      UrlFinder,
      SearchFinder,
    )
  }
}