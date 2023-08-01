package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.fixed.CharSequenceTerminateFinder
import com.github.samunohito.mfm.api.finder.inline.EmojiCodeFinder
import com.github.samunohito.mfm.api.finder.inline.UnicodeEmojiFinder

class SimpleFinder(
  terminateFinder: ISubstringFinder = CharSequenceTerminateFinder,
) : RecursiveFinderBase(terminateFinder) {
  override val finders: List<ISubstringFinder>
    get() = _finders
  override val foundType = FoundType.Simple

  companion object {
    private val _finders = listOf(
      UnicodeEmojiFinder,
      EmojiCodeFinder,
    )
  }
}