package com.github.samunohito.mfm.api.finder

import com.github.samunohito.mfm.api.finder.core.FoundType
import com.github.samunohito.mfm.api.finder.core.SequentialFinder
import com.github.samunohito.mfm.api.finder.core.StringFinder
import com.github.samunohito.mfm.api.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.api.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.api.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.api.utils.next

object CodeBlockFinder : ISubstringFinder {
  private val mark = StringFinder("```")
  private val codeBlockFinder = SequentialFinder(
    NewLineFinder.optional(),
    LineBeginFinder,
    mark,
    SequentialScanFinder.ofUntil(NewLineFinder).optional(),
    NewLineFinder,
    SequentialScanFinder.ofUntil(NewLineFinder, mark, LineEndFinder),
    NewLineFinder,
    mark,
    LineEndFinder,
    NewLineFinder.optional()
  )

  override fun find(input: String, startAt: Int, context: ISubstringFinderContext): ISubstringFinderResult {
    val result = codeBlockFinder.find(input, startAt, context)
    if (!result.success) {
      return failure()
    }

    val langResult = result.foundInfo.nestedInfos[3]
    val codeResult = result.foundInfo.nestedInfos[5]
    return success(
      FoundType.CodeBlock,
      result.foundInfo.overallRange,
      result.foundInfo.contentRange,
      result.foundInfo.overallRange.next(),
      listOf(
        if (langResult.contentRange.isEmpty()) {
          SubstringFoundInfo.EMPTY
        } else {
          SubstringFoundInfo(
            FoundType.CodeBlock,
            langResult.overallRange,
            langResult.contentRange,
            langResult.contentRange.next()
          )
        },
        SubstringFoundInfo(
          FoundType.CodeBlock,
          codeResult.overallRange,
          codeResult.contentRange,
          codeResult.contentRange.next()
        )
      )
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Lang(0),
    Code(1),
  }
}