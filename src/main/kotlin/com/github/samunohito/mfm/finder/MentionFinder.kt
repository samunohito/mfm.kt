package com.github.samunohito.mfm.finder

import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.utils.next
import com.github.samunohito.mfm.utils.offset

class MentionFinder : ISubstringFinder {
  companion object {
    private const val regexGroupUserName = "username"
    private const val regexGroupHostName = "hostname"
    private val regexMention = Regex("^@(?<$regexGroupUserName>[a-zA-Z0-9_-]+)(@(?<$regexGroupHostName>[a-z0-9_.-]+))?")
    private val regexAlphaNumericTail = Regex("[0-9a-z]", RegexOption.IGNORE_CASE)
    private val regexDotHyphenTail = Regex("[.-]+$")
    private val regexDotHyphenHead = Regex("^[.-]")
    private val regexHyphenTail = Regex("-+$")

    private fun findUsernameRange(input: String, usernameRange: IntRange): ISubstringFinderResult {
      val username = input.substring(usernameRange)

      // 先頭がハイフンで始まることを許容しない
      if (username[0] == '-') {
        return failure()
      }

      // 末尾がハイフンで終わっているかを調べる(ハイフンがない場合は除外の必要なしなのでそのまま返す)
      val hyphenTailMatch = regexHyphenTail.find(username)
        ?: return success(FoundType.Mention, usernameRange, usernameRange.next())

      // ハイフンしか無い場合はユーザー名として認識しない
      val modifiedUsername = username.substring(0 until hyphenTailMatch.range.first)
      if (modifiedUsername.isEmpty()) {
        return failure()
      }

      val modifiedUsernameRange = usernameRange.first until usernameRange.first + modifiedUsername.length
      return success(FoundType.Mention, modifiedUsernameRange, modifiedUsernameRange.next())
    }

    private fun findHostnameRange(input: String, hostnameRange: IntRange): ISubstringFinderResult {
      val hostname = input.substring(hostnameRange)

      // ドットまたはハイフンで始まっている場合は、ホスト名として認識しない
      if (regexDotHyphenHead.containsMatchIn(hostname)) {
        return failure()
      }

      // ドットまたはハイフンで終わっているかを調べる(ドットまたはハイフンがない場合は除外の必要なしなのでそのまま返す)
      val dotHyphenTailMatch = regexDotHyphenTail.find(hostname)
        ?: return success(FoundType.Mention, hostnameRange, hostnameRange.next())

      // ドットまたはハイフンしか無い場合はホスト名として認識しない
      val modifiedHostname = hostname.substring(0 until dotHyphenTailMatch.range.first)
      if (modifiedHostname.isEmpty()) {
        return failure()
      }

      val modifiedHostnameRange = hostnameRange.first until hostnameRange.first + modifiedHostname.length
      return success(FoundType.Mention, modifiedHostnameRange, modifiedHostnameRange.next())
    }
  }

  override fun find(input: String, startAt: Int): ISubstringFinderResult {
    val mentionMatch = regexMention.find(input.substring(startAt))
      ?: return failure()

    // メンションの直前が英数字だった場合、メンションとして認識しない
    if (startAt >= 1) {
      val beforeChar = input.substring(startAt - 1, startAt)
      if (regexAlphaNumericTail.containsMatchIn(beforeChar)) {
        return failure()
      }
    }

    val usernameRange = mentionMatch.groups[regexGroupUserName]?.range?.offset(startAt)
      ?: return failure()
    val hostnameRange = mentionMatch.groups[regexGroupHostName]?.range?.offset(startAt)
      ?: IntRange.EMPTY

    if (!hostnameRange.isEmpty()) {
      val hostnameResult = findHostnameRange(input, hostnameRange)
      if (!hostnameResult.success) {
        return failure()
      }

      val usernameResult = findUsernameRange(input, usernameRange)
      if (!usernameResult.success || usernameResult.foundInfo.range != usernameRange) {
        // 範囲が変わっている＝ハイフンのぶんを切り詰められたということ
        // ホスト名が存在するときは、ユーザ名の後ろのハイフンを切り捨てられない。
        // （切り捨ててTextとして認識させたいが、ホスト名がある時にそれをやるとホスト名までText扱いになってしまうので）
        return failure()
      }

      val mentionRange = usernameResult.foundInfo.range.first..hostnameResult.foundInfo.range.last
      return success(
        FoundType.Mention,
        mentionRange,
        mentionRange.next(),
        listOf(
          SubstringFoundInfo(FoundType.Mention, usernameResult.foundInfo.range, usernameResult.foundInfo.range.next()),
          SubstringFoundInfo(FoundType.Mention, hostnameResult.foundInfo.range, hostnameResult.foundInfo.range.next())
        )
      )
    } else {
      val usernameResult = findUsernameRange(input, usernameRange)
      if (!usernameResult.success) {
        return failure()
      }

      return success(
        FoundType.Mention,
        usernameResult.foundInfo.range,
        usernameResult.foundInfo.range.next(),
        listOf(
          SubstringFoundInfo(FoundType.Mention, usernameResult.foundInfo.range, usernameResult.foundInfo.range.next()),
          SubstringFoundInfo.EMPTY,
        )
      )
    }
  }

  enum class SubIndex(override val index: Int) : ISubIndex {
    Username(0),
    Hostname(1),
  }
}