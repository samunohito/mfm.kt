package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType

/**
 * Interface for substring finder.
 * Used to find substrings in a given input string.
 */
interface ISubstringFinder {
  /**
   * Finds a substring in the input string starting from a given index.
   *
   * @param input The input string to search.
   * @param startAt The index to start searching from. Defaults to 0.
   * @return The result of the substring search as an [ISubstringFinderResult].
   */
  fun find(input: String, startAt: Int = 0): ISubstringFinderResult

  /**
   * Returns an optional version of the current substring finder.
   * An optional finder will return a successful result even if the substring is not found.
   *
   * @return An [ISubstringFinder] instance that represents an optional finder.
   */
  fun optional(): ISubstringFinder {
    return Optional(this)
  }

  /**
   * Returns a negative (not) version of the current substring finder.
   * A negative finder will return a failure result if the substring is found.
   *
   * @return An [ISubstringFinder] instance that represents a negative finder.
   */
  fun not(): ISubstringFinder {
    return Not(this)
  }

  private class Optional(private val delegate: ISubstringFinder) : ISubstringFinder {
    /**
     * Finds a substring in the input string starting from a given index.
     * If the substring is not found, it returns a successful result with empty ranges.
     *
     * @param input The input string to search.
     * @param startAt The index to start searching from.
     * @return The result of the substring search as an [ISubstringFinderResult].
     */
    override fun find(input: String, startAt: Int): ISubstringFinderResult {
      val result = delegate.find(input, startAt)
      return if (result.success) {
        result
      } else {
        success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
      }
    }
  }

  private class Not(private val delegate: ISubstringFinder) : ISubstringFinder {
    /**
     * Finds a substring in the input string starting from a given index.
     * If the substring is found, it returns a failure result.
     *
     * @param input The input string to search.
     * @param startAt The index to start searching from.
     * @return The result of the substring search as an [ISubstringFinderResult].
     */
    override fun find(input: String, startAt: Int): ISubstringFinderResult {
      val result = delegate.find(input, startAt)
      return if (result.success) {
        failure()
      } else {
        success(FoundType.Core, result.foundInfo)
      }
    }
  }
}