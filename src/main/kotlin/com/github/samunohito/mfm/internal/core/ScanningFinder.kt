package com.github.samunohito.mfm.internal.core

class ScanningFinder(private val context: Context) : CharSequenceFinderBase() {
  override fun doScanning(text: String, startAt: Int): SubstringFinderResult {
    val foundIndex = text.indexOf(context.scanPeriod, startAt)
    return if (foundIndex <= -1) {
      SubstringFinderResult.ofFailure(text, IntRange.EMPTY, startAt + text.length)
    } else {
      val range = startAt until foundIndex
      SubstringFinderResult.ofSuccess(text, range, range.last + 1)
    }
  }

  data class Context(
    val scanPeriod: String
  )
}