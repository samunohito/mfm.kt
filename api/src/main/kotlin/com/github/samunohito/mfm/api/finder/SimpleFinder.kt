package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder

class SimpleFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
  context: IRecursiveFinderContext,
) : RecursiveFinderBase(terminateFinder, context) {
  override val finders: List<ISubstringFinder> = listOf(
    UnicodeEmojiFinder(),
    EmojiCodeFinder(),
  )
  override val foundType = FoundType.Simple
}