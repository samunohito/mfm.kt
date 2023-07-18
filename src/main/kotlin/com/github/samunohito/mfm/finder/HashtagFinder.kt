package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.*
import com.github.samunohito.mfm.finder.core.charsequence.AlternateScanFinder
import com.github.samunohito.mfm.finder.core.fixed.NewLineFinder
import com.github.samunohito.mfm.finder.core.fixed.SpaceFinder
import com.github.samunohito.mfm.utils.merge
import com.github.samunohito.mfm.utils.next

class HashtagFinder : ISubstringFinder {
  companion object {
    private val regexAlphaAndNumericTail = Regex("[a-z0-9]$", RegexOption.IGNORE_CASE)
    private val regexNumericOnly = Regex("^[0-9]+$")
    private val mark = StringFinder("#")
    private val hashtagFinder = SequentialFinder(mark, HashtagBodyFinder)

    private object HashtagBodyFinder : ISubstringFinder {
      private val openRegexBracket = RegexFinder(Regex("[(\\[「（]"))
      private val closeRegexBracket = RegexFinder(Regex("[)\\]」）]"))
      private val nestableFinder = AlternateFinder(
        // パターンにカッコを含めると、終了カッコのみを誤検出してしまう。
        // …ので、サポートされている種類の開始・終了カッコが揃っている場合は、その中身を再帰的に検索する
        SequentialFinder(
          openRegexBracket,
          HashtagBodyFinder,
          closeRegexBracket,
        ),
        // このパターンに合致する文字が登場するまでを繰り返し検索する
        AlternateScanFinder.ofWhile(
          SpaceFinder,
          NewLineFinder,
          RegexFinder(Regex("[ \\u3000\\t.,!?'\"#:/\\[\\]【】()「」（）<>]")),
        )
      )

      override fun find(input: String, startAt: Int): ISubstringFinderResult {
        var latestIndex = startAt
        val foundInfos = mutableListOf<SubstringFoundInfo>()

        while (true) {
          val result = nestableFinder.find(input, latestIndex)
          if (!result.success) {
            break
          }

          foundInfos.add(result.foundInfo)
          latestIndex = result.foundInfo.next
        }

        if (foundInfos.isEmpty()) {
          return failure()
        }

        val range = foundInfos.map { it.range }.merge()
        return success(FoundType.Hashtag, range, range.next(), foundInfos)
      }
    }
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val result = hashtagFinder.find(input, startAt)
    if (!result.success) {
      return failure()
    }

    // ハッシュタグの直前が英数字の場合はハッシュタグとして認識しない
    val beforeStr = input.substring(0 until startAt)
    if (regexAlphaAndNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    // 検出された文字が数値のみの場合はハッシュタグではない
    val hashtagNameResult = result.foundInfo.sub[1]
    val hashtagName = input.substring(hashtagNameResult.range)
    if (regexNumericOnly.containsMatchIn(hashtagName)) {
      return failure()
    }

    return success(FoundType.Hashtag, hashtagNameResult.range, result.foundInfo.next)
  }
}