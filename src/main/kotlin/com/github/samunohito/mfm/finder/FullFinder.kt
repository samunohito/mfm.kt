package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType

class FullFinder(
  terminateFinder: ISubstringFinder,
  callback: Callback = Callback.impl,
) : RecursiveFinderBase(terminateFinder, callback) {
  override val finders: List<ISubstringFinder> = listOf(
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
  override val foundType = FoundType.Full
}