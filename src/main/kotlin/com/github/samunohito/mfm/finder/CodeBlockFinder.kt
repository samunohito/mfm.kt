package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.finder.core.charsequence.SequentialScanFinder
import com.github.samunohito.mfm.finder.core.fixed.LineBeginFinder
import com.github.samunohito.mfm.finder.core.fixed.LineEndFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.utils.next

class CodeBlockFinder : ISubstringFinder {
  companion object {
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
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = codeBlockFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val langResult = result.foundInfo.sub[3]
    val codeResult = result.foundInfo.sub[5]
    return success(
      FoundType.CodeBlock,
      result.foundInfo.range,
      result.foundInfo.range.next(),
      listOf(
        if (langResult.range.isEmpty()) {
          SubstringFoundInfo.EMPTY
        } else {
          SubstringFoundInfo(FoundType.CodeBlock, langResult.range, langResult.range.next())
        },
        SubstringFoundInfo(FoundType.CodeBlock, codeResult.range, codeResult.range.next())
      )
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Lang(0),
    Code(1),
  }
}