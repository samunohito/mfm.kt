package com.github.samunohito.mfm

import com.github.samunohito.mfm.internal.core.*
import com.github.samunohito.mfm.internal.core.singleton.NewLineFinder
import com.github.samunohito.mfm.internal.core.singleton.SpaceFinder
import com.github.samunohito.mfm.node.MfmHashtag

class HashtagParser(private val context: Context = defaultContext) : IParser<MfmHashtag> {
  private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
  private val regexNumericOnly = Regex("^[0-9]+$")

  companion object {
    private val defaultContext: Context = Context.init()
    private val markFinder = StringFinder("#")

    private class HashtagFinder(private val context: Context) : ISubstringFinder {
      companion object {
        private val openRegexBracket = RegexFinder(Regex("[(\\[「（]"))
        private val closeRegexBracket = RegexFinder(Regex("[)\\]」）]"))
      }

      private object UsableCharacterFinder : CharSequenceFinderBase() {
        private val unusableCharactersFinder = RegexFinder(Regex("[ \\u3000\\t.,!?'\"#:/\\[\\]【】()「」（）<>]"))

        override fun hasNext(text: String, startAt: Int): Boolean {
          val unusable = unusableCharactersFinder.find(text, startAt)
          if (unusable.success) {
            return false
          }

          val space = SpaceFinder.find(text, startAt)
          if (space.success) {
            return false
          }

          val newLine = NewLineFinder.find(text, startAt)
          if (newLine.success) {
            return false
          }

          return true
        }
      }

      override fun find(input: String, startAt: Int): SubstringFinderResult {
        if (context.currentDepth > context.recursiveDepthLimit) {
          return SubstringFinderResult.ofFailure()
        }

        var latestIndex = startAt
        val bodyResults = mutableListOf<SubstringFinderResult>()

        while (true) {
          // wordFinderのパターンにカッコを含めると、終了カッコのみを誤検出してしまう。
          // …ので、サポートされている種類の開始・終了カッコが揃っている場合は、その中身を再帰的に検索する
          val regexBracketWrappedFinders = listOf(
            openRegexBracket,
            HashtagFinder(context.incrementDepth()),
            closeRegexBracket,
          )
          val bracketResult = SubstringFinderUtils.sequential(input, latestIndex, regexBracketWrappedFinders)
          if (bracketResult.success) {
            bodyResults.add(bracketResult)
            latestIndex = bracketResult.next
            continue
          }

          val usableCharactersResult = UsableCharacterFinder.find(input, latestIndex)
          if (usableCharactersResult.success) {
            bodyResults.add(usableCharactersResult)
            latestIndex = usableCharactersResult.next
            continue
          }

          break
        }

        if (bodyResults.isEmpty()) {
          return SubstringFinderResult.ofFailure()
        }

        val bodyRange = bodyResults.first().range.first..bodyResults.last().range.last
        return SubstringFinderResult.ofSuccess(input, bodyRange, bodyRange.last + 1, bodyResults)
      }
    }
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmHashtag> {
    val result = SubstringFinderUtils.sequential(input, startAt, listOf(markFinder, HashtagFinder(context)))
    if (!result.success) {
      return ParserResult.ofFailure()
    }

    // ハッシュタグの直前が英数字の場合はハッシュタグとして認識しない
    val beforeStr = input.substring(0 until startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return ParserResult.ofFailure()
    }

    // 検出された文字が数値のみの場合はハッシュタグではない
    val hashtagNameResult = result.nests[1]
    val hashtagName = input.substring(hashtagNameResult.range)
    if (regexNumericOnly.containsMatchIn(hashtagName)) {
      return ParserResult.ofFailure()
    }

    return ParserResult.ofSuccess(MfmHashtag(hashtagName), input, hashtagNameResult.range, result.next)
  }

  class Context private constructor(
    var disabled: Boolean,
    val currentDepth: Int,
    val recursiveDepthLimit: Int,
  ) {
    companion object {
      fun init(recursiveDepthLimit: Int = 20): Context {
        return Context(
          disabled = false,
          currentDepth = 0,
          recursiveDepthLimit = recursiveDepthLimit
        )
      }
    }

    fun incrementDepth(): Context {
      return Context(
        disabled = disabled,
        currentDepth = currentDepth + 1,
        recursiveDepthLimit = recursiveDepthLimit
      )
    }
  }
}