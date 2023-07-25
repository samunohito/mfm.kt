package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder

class SimpleFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
  callback: Callback = Callback.impl,
) : RecursiveFinderBase(terminateFinder, callback) {
  override val finders: List<ISubstringFinder> = listOf(
    UnicodeEmojiFinder(),
    EmojiCodeFinder(),
  )
  override val foundType = FoundType.Simple
}