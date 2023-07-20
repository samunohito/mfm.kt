package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.finder.core.RegexFinder
import com.github.samunohito.mfm.finder.core.SequentialFinder
import com.github.samunohito.mfm.finder.core.StringFinder
import com.github.samunohito.mfm.utils.merge
import com.github.samunohito.mfm.utils.next

class FnFinder : ISubstringFinder {
  companion object {
    private val open = StringFinder("$[")
    private val close = StringFinder("]")
    private val nameFinder = RegexFinder(Regex("[a-z0-9_]+", RegexOption.IGNORE_CASE))
    private val argsFinder = SequentialFinder(
      StringFinder("."),
      ArgsFinder,
    )
    private val funcFinder = SequentialFinder(
      open,
      nameFinder,
      argsFinder.optional(),
      StringFinder(" "),
      InlineFinder(close),
      close,
    )

    private object ArgsFinder : ISubstringFinder {
      private val argFinder = SequentialFinder(
        RegexFinder(Regex("[a-z0-9_]+", RegexOption.IGNORE_CASE)),
        SequentialFinder(
          StringFinder("="),
          RegexFinder(Regex("[a-z0-9_.-]+", RegexOption.IGNORE_CASE)),
        ).optional()
      )
      private val argSeparator = StringFinder(",")

      override fun find(input: String, startAt: Int): ISubstringFinderResult {
        var latestIndex = startAt
        val args = mutableListOf<SubstringFoundInfo>()
        while (true) {
          val argResult = argFinder.find(input, latestIndex)
          if (argResult.success) {
            args.add(argResult.foundInfo)
            latestIndex = argResult.foundInfo.next
          } else {
            val separatorResult = argSeparator.find(input, latestIndex)
            if (separatorResult.success) {
              latestIndex = separatorResult.foundInfo.next
            } else {
              break
            }
          }
        }

        val range = args.map { it.range }.merge()
        return success(FoundType.Fn, range, range.next(), args)
      }
    }
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = funcFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    val name = result.foundInfo[1]
    val args = result.foundInfo[2].let {
      if (it.range.isEmpty()) {
        SubstringFoundInfo.EMPTY
      } else {
        it
      }
    }
    val content = result.foundInfo[4]

    return success(
      FoundType.Fn,
      result.foundInfo.range,
      result.foundInfo.next,
      listOf(name, args, content)
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Name(0),
    Args(1),
    Content(2),
  }
}