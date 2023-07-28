package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

abstract class RecursiveFinderBase(
  private val terminateFinder: ISubstringFinder,
  private val context: IRecursiveFinderContext,
) : NestedFinderBase(context) {
  protected abstract val finders: List<ISubstringFinder>
  protected abstract val foundType: FoundType

  override fun doFind(input: String, startAt: Int): ISubstringFinderResult {
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
      success(foundType, range, latestIndex, foundInfos)
    }
  }

  private fun shouldTerminate(input: String, latestIndex: Int): Boolean {
    if (input.length < latestIndex) {
      return true
    }

    return terminateFinder.find(input, latestIndex).success
  }

  private fun findWithFactories(input: String, latestIndex: Int): ISubstringFinderResult {
    for (finder in finders) {
      if (!context.excludeFinders.contains(finder::class.java)) {
        val result = finder.find(input, latestIndex)
        if (result.success) {
          return result
        }
      }
    }
    return failure()
  }
}