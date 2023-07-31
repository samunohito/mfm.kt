package com.github.samunohito.mfm.api.finder

/**
 * An [ISubstringFinder] implementation for detecting "Unicode Emoji" syntax.
 * Emojis defined in Unicode are search results.
 */
object UnicodeEmojiFinder : ISubstringFinder {
  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    // TODO: Implement
    return failure()
  }
}