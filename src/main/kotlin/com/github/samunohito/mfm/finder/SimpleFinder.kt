package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType

class SimpleFinder(
  terminateFinder: ISubstringFinder,
  callback: Callback = Callback.impl,
) : RecursiveFinderBase(terminateFinder, callback) {
  override val finders: List<ISubstringFinder> = listOf(
    UnicodeEmojiFinder(),
    EmojiCodeFinder(),
  )
  override val foundType = FoundType.Inline
}