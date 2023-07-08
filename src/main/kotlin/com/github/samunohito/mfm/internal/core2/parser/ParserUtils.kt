package com.github.samunohito.mfm.internal.core2.parser

object ParserUtils {
  fun <T> sequential(input: String, startAt: Int, parsers: List<IParser2<T>>): Result2<List<T>> {
    val text = input.slice(startAt..input.length)
    var latestIndex = startAt
    val results = mutableListOf<T>()

    for (parser in parsers) {
      val result = parser.parse(text, latestIndex)
      if (!result.success) {
        return Result2.ofFailure()
      }

      latestIndex = result.range.last
      results.add(result.value!!)
    }

    return Result2.ofSuccess(startAt..latestIndex, results)
  }

  fun <T> alternative(input: String, startAt: Int, parsers: List<IParser2<T>>): Result2<T> {
    for (parser in parsers) {
      val result = parser.parse(input, startAt)
      if (result.success) {
        return result
      }
    }

    return Result2.ofFailure()
  }

  fun <T> alternative(input: String, startAt: Int, vararg parsers: IParser2<T>): Result2<T> {
    return alternative(input, startAt, parsers.toList())
  }
}