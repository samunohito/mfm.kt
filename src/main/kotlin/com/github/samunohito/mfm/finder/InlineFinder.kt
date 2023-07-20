package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.fixed.CharSequenceTerminateFinder

class InlineFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
  callback: Callback = Callback.impl,
) : RecursiveFinderBase(terminateFinder, callback) {
  override val finders: List<ISubstringFinder> = listOf(
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
  override val foundType = FoundType.Inline
}