package com.github.samunohito.mfm.api.parser

import com.github.samunohito.mfm.api.parser.core.FoundType

/**
 * Interface for MFM parser.
 * Used to find format in a given input string.
 */
interface IMfmParser {
  /**
   * Finds a substring in the input string starting from a given index.
   *
   * @param input The input string to search.
   * @param startAt The index to start searching from. Defaults to 0.
   * @return The result of the substring search as an [IMfmParserResult].
   */
  fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult

  /**
   * Returns an optional version of the current substring parser.
   * An optional parser will return a successful result even if the substring is not found.
   *
   * @return An [IMfmParser] instance that represents an optional parser.
   */
  fun optional(): IMfmParser {
    return Optional(this)
  }

  /**
   * Returns a negative (not) version of the current MFM parser.
   * A negative parser will return a failure result if the substring is found.
   *
   * @return An [IMfmParser] instance that represents a negative parser.
   */
  fun not(): IMfmParser {
    return Not(this)
  }

  private class Optional(private val delegate: IMfmParser) : IMfmParser {
    /**
     * Finds a substring in the input string starting from a given index.
     * If the substring is not found, it returns a successful result with empty ranges.
     *
     * @param input The input string to search.
     * @param startAt The index to start searching from.
     * @return The result of the substring search as an [IMfmParserResult].
     */
    override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
      val result = delegate.find(input, startAt, context)
      return if (result.success) {
        result
      } else {
        success(FoundType.Core, IntRange.EMPTY, IntRange.EMPTY, startAt)
      }
    }
  }

  private class Not(private val delegate: IMfmParser) : IMfmParser {
    /**
     * Finds a substring in the input string starting from a given index.
     * If the substring is found, it returns a failure result.
     *
     * @param input The input string to search.
     * @param startAt The index to start searching from.
     * @return The result of the substring search as an [IMfmParserResult].
     */
    override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
      val result = delegate.find(input, startAt, context)
      return if (result.success) {
        failure()
      } else {
        success(FoundType.Core, result.foundInfo)
      }
    }
  }
}