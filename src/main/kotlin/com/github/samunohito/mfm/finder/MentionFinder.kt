package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.utils.next
import com.github.samunohito.mfm.utils.offset

class MentionFinder : ISubstringFinder {
  companion object {
    private const val regexGroupUserName = "username"
    private const val regexGroupHostName = "hostname"
    private val regexMention = Regex("^@(?<$regexGroupUserName>[a-zA-Z0-9_-]+)(@(?<$regexGroupHostName>[a-z0-9_.-]+))?")
    private val regexAlphaNumericTail = Regex("[0-9a-z]$", RegexOption.IGNORE_CASE)
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val mentionMatch = regexMention.find(input.substring(startAt))
      ?: return failure()

    // メンションの直前が英数字だった場合、メンションとして認識しない
    val beforeStr = input.substring(0 until startAt)
    if (regexAlphaNumericTail.containsMatchIn(beforeStr)) {
      return failure()
    }

    val usernameGroup = mentionMatch.groups[regexGroupUserName]
      ?: return failure()
    val hostnameGroup = mentionMatch.groups[regexGroupHostName]

    val mentionRange = mentionMatch.range.offset(startAt)
    val usernameRange = usernameGroup.range.offset(startAt)
    return success(
      FoundType.Mention,
      mentionRange,
      mentionRange.next(),
      listOf(
        SubstringFoundInfo(FoundType.Mention, usernameRange, usernameRange.next()),
        hostnameGroup?.let { SubstringFoundInfo(FoundType.Mention, it.range.offset(startAt), it.range.next()) }
          ?: SubstringFoundInfo.EMPTY
      )
    )
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Username(0),
    Hostname(1),
  }
}