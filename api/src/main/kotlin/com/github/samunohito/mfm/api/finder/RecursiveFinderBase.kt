package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

abstract class RecursiveFinderBase(
  private val terminateFinder: ISubstringFinder,
  private val callback: Callback = Callback.impl
) : ISubstringFinder {
  protected abstract val finders: List<ISubstringFinder>
  protected abstract val foundType: FoundType

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    var textNodeStartAt = startAt
    var latestIndex = startAt
    val foundInfos = mutableListOf<SubstringFoundInfo>()

    while (!shouldTerminate(input, latestIndex)) {
      val findResult = findWithFactories(input, latestIndex)
      if (findResult.success) {
        if (textNodeStartAt != latestIndex) {
          val range = textNodeStartAt until latestIndex
          foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range.last + 1))
        }
        foundInfos.add(findResult.foundInfo)

        textNodeStartAt = findResult.foundInfo.next
        latestIndex = findResult.foundInfo.next
      } else {
        latestIndex++
      }
    }

    return if (startAt == latestIndex) {
      failure()
    } else {
      if (textNodeStartAt != latestIndex) {
        // ここに来てprevとlatestに差がある場合、リストに登録してないTextが存在するということ
        val range = textNodeStartAt until latestIndex
        foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range.last + 1))
      }

      val range = foundInfos.map { it.range }.merge()
      success(foundType, range, range.next(), foundInfos)
    }
  }

  private fun shouldTerminate(input: String, latestIndex: Int): Boolean {
    return terminateFinder.find(input, latestIndex).success
  }

  private fun findWithFactories(input: String, latestIndex: Int): ISubstringFinderResult {
    for (finder in finders) {
      if (callback.needProcess(input, latestIndex, finder)) {
        val result = finder.find(input, latestIndex)
        if (result.success) {
          return result
        }
      }
    }
    return failure()
  }

  interface Callback {
    fun needProcess(input: String, startAt: Int, finder: ISubstringFinder): Boolean

    companion object {
      val impl = object : Callback {
        override fun needProcess(input: String, startAt: Int, finder: ISubstringFinder): Boolean {
          return true
        }
      }
    }
  }
}