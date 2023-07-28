package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder

class FullFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
  context: IRecursiveFinderContext,
) : RecursiveFinderBase(terminateFinder, context) {
  override val finders: List<ISubstringFinder>
    get() = lazyFinders.value
  override val foundType = FoundType.Full

  private val lazyFinders = lazy {
    listOf(
      UnicodeEmojiFinder(),
      CenterTagFinder(context),
      SmallTagFinder(context),
      PlainTagFinder(),
      BoldTagFinder(context),
      ItalicTagFinder(context),
      StrikeTagFinder(context),
      UrlAltFinder(),
      BigFinder(context),
      BoldAstaFinder(context),
      ItalicAstaFinder(),
      BoldUnderFinder(),
      ItalicUnderFinder(),
      CodeBlockFinder(),
      InlineCodeFinder(),
      QuoteFinder(),
      MathBlockFinder(),
      MathInlineFinder(),
      StrikeWaveFinder(context),
      FnFinder(context),
      MentionFinder(),
      HashtagFinder(),
      EmojiCodeFinder(),
      LinkFinder(context),
      UrlFinder(),
      SearchFinder(),
    )
  }
}