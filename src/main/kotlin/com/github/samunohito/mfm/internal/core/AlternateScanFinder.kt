package com.github.samunohito.mfm.internal.core

class AlternateScanFinder private constructor(
  private val terminates: Collection<ISubstringFinder>,
  isWhile: Boolean
) : ScanFinderBase() {
  private val handler: (SubstringFinderResult) -> Boolean = if (isWhile) {
    { it.success }
  } else {
    { !it.success }
  }

  override fun hasNext(text: String, startAt: Int): Boolean {
    val result = SubstringFinderUtils.alternate(text, startAt, terminates)
    return handler(result)
  }

  companion object {
    fun ofWhile(terminates: Collection<ISubstringFinder>): AlternateScanFinder {
      return AlternateScanFinder(terminates, true)
    }

    fun ofUntil(terminates: Collection<ISubstringFinder>): AlternateScanFinder {
      return AlternateScanFinder(terminates, false)
    }
  }
}