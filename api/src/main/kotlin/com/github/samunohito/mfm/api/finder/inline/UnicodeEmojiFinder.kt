package com.github.samunohito.mfm.api.finder.inline

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderContext
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.failure

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