package com.github.samunohito.mfm.api.parser

import com.github.samunohito.mfm.api.parser.core.FoundType


/**
 * Interface for the result of MFM parser operation.
 */
interface IMfmParserResult {
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
 * Private data class for the implementation of the [IMfmParserResult] interface.
 */
private data class Impl(
  override val success: Boolean,
  override val foundInfo: SubstringFoundInfo,
) : IMfmParserResult {
  companion object {
    /**
     * Creates an instance of Impl with a success flag and found information.
     */
    @JvmStatic
    fun ofSuccess(result: SubstringFoundInfo): IMfmParserResult {
      return Impl(true, result)
    }

    /**
     * Creates an instance of Impl with a failure flag and an empty found information.
     */
    @JvmStatic
    fun ofFailure(): IMfmParserResult {
      return Impl(false, SubstringFoundInfo.EMPTY)
    }
  }
}

/**
 * Creates an instance of [IMfmParserResult] with a success flag and found information.
 */
fun success(result: SubstringFoundInfo): IMfmParserResult {
  return Impl.ofSuccess(result)
}

/**
 * Creates an instance of [IMfmParserResult] with a success flag, found information, and a list of nested information.
 *
 * @see SubstringFoundInfo
 */
fun success(
  foundType: FoundType,
  overallRange: IntRange,
  contentRange: IntRange,
  resumeIndex: Int,
  nestedInfos: List<SubstringFoundInfo> = listOf()
): IMfmParserResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, overallRange, contentRange, resumeIndex, nestedInfos))
}

/**
 * Creates an instance of [IMfmParserResult] with a success flag and a nested result.
 */
fun success(foundType: FoundType, result: IMfmParserResult): IMfmParserResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, result.foundInfo))
}

/**
 * Creates an instance of [IMfmParserResult] with a success flag and found information.
 */
fun success(foundType: FoundType, info: SubstringFoundInfo): IMfmParserResult {
  return Impl.ofSuccess(SubstringFoundInfo(foundType, info))
}

/**
 * Creates an instance of [IMfmParserResult] with a failure flag and an empty found information.
 */
fun failure(): IMfmParserResult {
  return Impl.ofFailure()
}
