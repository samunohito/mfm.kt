package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.utils.merge
import com.github.samunohito.mfm.api.utils.next

abstract class RecursiveFinderBase(
  private val terminateFinder: ISubstringFinder
) : ISubstringFinder {
  protected abstract val finders: List<ISubstringFinder>
  protected abstract val foundType: FoundType

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    var textNodeStartAt = startAt
    var latestIndex = startAt
    val foundInfos = mutableListOf<SubstringFoundInfo>()

    while (!shouldTerminate(input, latestIndex, context)) {
      val findResult = findWithFinders(input, latestIndex, context)
      if (findResult.success) {
        if (textNodeStartAt != latestIndex) {
          // 前回見つかったノードと今回見つかったノードの間にある文字たちをTextノードとして登録する
          val range = textNodeStartAt until latestIndex
          foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range, range.last + 1))
        }
        foundInfos.add(findResult.foundInfo)

        textNodeStartAt = findResult.foundInfo.resumeIndex
        latestIndex = findResult.foundInfo.resumeIndex
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
        foundInfos.add(SubstringFoundInfo(FoundType.Text, range, range, range.last + 1))
      }

      val fullRange = startAt until latestIndex
      val contentRange = foundInfos.map { it.contentRange }.merge()
      success(foundType, fullRange, contentRange, fullRange.next(), foundInfos)
    }
  }

  private fun shouldTerminate(
    input: String,
    latestIndex: Int,
    context: ISubstringFinderContext
  ): Boolean {
    if (input.length < latestIndex) {
      return true
    }

    return terminateFinder.find(input, latestIndex, context).success
  }

  private fun findWithFinders(
    input: String,
    latestIndex: Int,
    context: ISubstringFinderContext
  ): ISubstringFinderResult {
    for (finder in finders) {
      val result = if (finder::class.java in context.excludeFinders) {
        failure()
      } else {
        finder.find(input, latestIndex, context)
      }

      if (result.success) {
        return result
      }
    }
    return failure()
  }
}