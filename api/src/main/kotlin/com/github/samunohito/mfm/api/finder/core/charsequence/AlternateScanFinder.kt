package com.github.samunohito.mfm.api.finder.core.charsequence

import com.github.samunohito.mfm.api.finder.ISubstringFinder
import com.github.samunohito.mfm.api.finder.ISubstringFinderResult
import com.github.samunohito.mfm.api.finder.core.utils.SubstringFinderUtils

class AlternateScanFinder private constructor(
  private val terminates: Collection<ISubstringFinder>,
  isWhile: Boolean
) : ScanFinderBase() {
  private val handler: (ISubstringFinderResult) -> Boolean = if (isWhile) {
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

    fun ofWhile(vararg terminates: ISubstringFinder): AlternateScanFinder {
      return AlternateScanFinder(terminates.toList(), true)
    }

    fun ofUntil(terminates: Collection<ISubstringFinder>): AlternateScanFinder {
      return AlternateScanFinder(terminates, false)
    }

    fun ofUntil(vararg terminates: ISubstringFinder): AlternateScanFinder {
      return AlternateScanFinder(terminates.toList(), false)
    }
  }
}