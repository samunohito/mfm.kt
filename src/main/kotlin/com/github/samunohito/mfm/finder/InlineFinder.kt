package com.github.samunohito.mfm.finder

class InlineFinder(
  terminateFinder: ISubstringFinder,
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
}