package com.github.samunohito.mfm.internal.core

class ScanningFinder(private val context: Context) : ISubstringFinder {
  override fun find(input: String, startAt: Int): SubstringFinderResult {
    var lastIndex: Int = startAt
    for (i in startAt until input.length) {
      if (context.scanFinishCondition.invoke(input[i])) {
        lastIndex = i
        break
      }
    }

    return SubstringFinderResult.ofSuccess(input, startAt until lastIndex, lastIndex)
  }

  data class Context(
    val scanFinishCondition: (Char) -> Boolean
  )
}