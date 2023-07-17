package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType

interface ISubstringFinderResult {
  val success: Boolean
  val foundInfo: SubstringFoundInfo
}

private class Impl private constructor(
  override val success: Boolean,
  override val foundInfo: SubstringFoundInfo,
) : ISubstringFinderResult {
  companion object {
    fun ofSuccess(result: SubstringFoundInfo): ISubstringFinderResult {
      return Impl(true, result)
    }

    fun ofSuccess(
      foundType: FoundType,
      range: IntRange,
      next: Int,
    ): ISubstringFinderResult {
      return ofSuccess(SubstringFoundInfo(foundType, range, next, listOf()))
    }

    fun ofSuccess(
      foundType: FoundType,
      range: IntRange,
      next: Int,
      sub: List<ISubstringFinderResult> = listOf()
    ): ISubstringFinderResult {
      return ofSuccess(SubstringFoundInfo(foundType, range, next, sub.map { it.foundInfo }))
    }

    fun ofSuccess(
      foundType: FoundType,
      range: IntRange,
      next: Int,
      sub: List<SubstringFoundInfo> = listOf()
    ): ISubstringFinderResult {
      return ofSuccess(SubstringFoundInfo(foundType, range, next, sub))
    }

    fun ofSuccess(foundType: FoundType, result: ISubstringFinderResult): ISubstringFinderResult {
      return ofSuccess(SubstringFoundInfo(foundType, result.foundInfo))
    }

    fun ofFailure(): ISubstringFinderResult {
      return Impl(false, SubstringFoundInfo.EMPTY)
    }
  }
}

fun success(result: SubstringFoundInfo): ISubstringFinderResult {
  return Impl.ofSuccess(result)
}

fun success(
  foundType: FoundType,
  range: IntRange,
  next: Int,
): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, range, next, listOf()))
}

fun success(
  foundType: FoundType,
  range: IntRange,
  next: Int,
  sub: List<ISubstringFinderResult> = listOf()
): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, range, next, sub.map { it.foundInfo }))
}

fun success(
  foundType: FoundType,
  range: IntRange,
  next: Int,
  sub: List<SubstringFoundInfo> = listOf()
): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, range, next, sub))
}

fun success(foundType: FoundType, result: ISubstringFinderResult): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, result.foundInfo))
}

fun success(foundType: FoundType, info: SubstringFoundInfo): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, info))
}

fun failure(): ISubstringFinderResult {
  return Impl.ofFailure()
}