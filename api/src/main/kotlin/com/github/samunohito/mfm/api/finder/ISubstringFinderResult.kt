package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType


/**
 * Interface for the result of a substring finder operation.
 */
interface ISubstringFinderResult {
  /**
   * Returns true if the substring was found successfully, false otherwise.
   */
  val success: Boolean

  /**
   * Returns information about the substring that was found, if any.
   */
  val foundInfo: SubstringFoundInfo
}

/**
 * Private data class for the implementation of the ISubstringFinderResult interface.
 */
private data class Impl(
  override val success: Boolean,
  override val foundInfo: SubstringFoundInfo,
) : ISubstringFinderResult {
  companion object {
    /**
     * Creates an instance of Impl with a success flag and found information.
     */
    @JvmStatic
    fun ofSuccess(result: SubstringFoundInfo): ISubstringFinderResult {
      return Impl(true, result)
    }

    /**
     * Creates an instance of Impl with a failure flag and an empty found information.
     */
    @JvmStatic
    fun ofFailure(): ISubstringFinderResult {
      return Impl(false, SubstringFoundInfo.EMPTY)
    }
  }
}

/**
 * Creates an instance of ISubstringFinderResult with a success flag and found information.
 */
fun success(result: SubstringFoundInfo): ISubstringFinderResult {
  return Impl.ofSuccess(result)
}

/**
 * Creates an instance of ISubstringFinderResult with a success flag, found information, and a list of nested information.
 *
 * @see SubstringFoundInfo
 */
fun success(
  foundType: FoundType,
  overallRange: IntRange,
  contentRange: IntRange,
  resumeIndex: Int,
  nestedInfos: List<SubstringFoundInfo> = listOf()
): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, overallRange, contentRange, resumeIndex, nestedInfos))
}

/**
 * Creates an instance of ISubstringFinderResult with a success flag and a nested result.
 */
fun success(foundType: FoundType, result: ISubstringFinderResult): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, result.foundInfo))
}

/**
 * Creates an instance of ISubstringFinderResult with a success flag and found information.
 */
fun success(foundType: FoundType, info: SubstringFoundInfo): ISubstringFinderResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, info))
}

/**
 * Creates an instance of ISubstringFinderResult with a failure flag and an empty found information.
 */
fun failure(): ISubstringFinderResult {
  return Impl.ofFailure()
}
