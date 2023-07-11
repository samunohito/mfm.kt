package com.github.samunohito.mfm.internal.core

class SequentialScanFinder private constructor(
  private val terminates: Collection<ISubstringFinder>
) : ScanFinderBase() {
  override fun hasNext(text: String, startAt: Int): Boolean {
    val result = SubstringFinderUtils.sequential(text, startAt, terminates)
    return !result.success
  }

  companion object {
    fun ofUntil(terminates: Collection<ISubstringFinder>): SequentialScanFinder {
      return SequentialScanFinder(terminates)
    }
  }
}