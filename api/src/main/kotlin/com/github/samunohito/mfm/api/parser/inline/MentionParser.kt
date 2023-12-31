package com.github.samunohito.mfm.api.parser.inline

import com.github.samunohito.mfm.api.parser.*
import com.github.samunohito.mfm.api.parser.core.FoundType
import com.github.samunohito.mfm.api.utils.next
import com.github.samunohito.mfm.api.utils.offset

/**
 * An [IMfmParser] implementation for detecting "mention" syntax.
 * The string starting with "@" will be the search result.
 *
 * ### Notes
 * - If the first character before the @ matches [a-z0-9] (case-insensitive) it will not be recognized as a mention.
 * - Username must be at least 1 letter.
 * - [a-z0-9_-] (case-insensitive) can be used for the Username.
 * - Username cannot start or end with "-".
 * - Host name must be at least 1 character.
 * - [a-z0-9_-.] (not case sensitive) can be used in the hostname.
 * - The host name cannot start or end with '-' or '.'
 */
object MentionParser : IMfmParser {
  private const val regexGroupUserName = "username"
  private const val regexGroupHostName = "hostname"
  private val regexMention = Regex("^@(?<$regexGroupUserName>[a-zA-Z0-9_-]+)(@(?<$regexGroupHostName>[a-z0-9_.-]+))?")
  private val regexAlphaNumericTail = Regex("[0-9a-z]", RegexOption.IGNORE_CASE)
  private val regexDotHyphenTail = Regex("[.-]+$")
  private val regexDotHyphenHead = Regex("^[.-]")
  private val regexHyphenTail = Regex("-+$")

  private fun findUsernameRange(input: String, usernameRange: IntRange): IMfmParserResult {
    val username = input.substring(usernameRange)

    // 先頭がハイフンで始まることを許容しない
    if (username[0] == '-') {
      return failure()
    }

    // 末尾がハイフンで終わっているかを調べる(ハイフンがない場合は除外の必要なしなのでそのまま返す)
    val hyphenTailMatch = regexHyphenTail.find(username)
      ?: return success(FoundType.Mention, usernameRange, usernameRange, usernameRange.next())

    // ハイフンしか無い場合はユーザー名として認識しない
    val modifiedUsername = username.substring(0 until hyphenTailMatch.range.first)
    if (modifiedUsername.isEmpty()) {
      return failure()
    }

    val modifiedUsernameRange = usernameRange.first until usernameRange.first + modifiedUsername.length
    return success(FoundType.Mention, modifiedUsernameRange, modifiedUsernameRange, modifiedUsernameRange.next())
  }

  private fun findHostnameRange(input: String, hostnameRange: IntRange): IMfmParserResult {
    val hostname = input.substring(hostnameRange)

    // ドットまたはハイフンで始まっている場合は、ホスト名として認識しない
    if (regexDotHyphenHead.containsMatchIn(hostname)) {
      return failure()
    }

    // ドットまたはハイフンで終わっているかを調べる(ドットまたはハイフンがない場合は除外の必要なしなのでそのまま返す)
    val dotHyphenTailMatch = regexDotHyphenTail.find(hostname)
      ?: return success(FoundType.Mention, hostnameRange, hostnameRange, hostnameRange.next())

    // ドットまたはハイフンしか無い場合はホスト名として認識しない
    val modifiedHostname = hostname.substring(0 until dotHyphenTailMatch.range.first)
    if (modifiedHostname.isEmpty()) {
      return failure()
    }

    val modifiedHostnameRange = hostnameRange.first until hostnameRange.first + modifiedHostname.length
    return success(FoundType.Mention, modifiedHostnameRange, modifiedHostnameRange, modifiedHostnameRange.next())
  }

  override fun find(input: String, startAt: Int, context: IMfmParserContext): IMfmParserResult {
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
      if (!usernameResult.success || usernameResult.foundInfo.contentRange != usernameRange) {
        // 範囲が変わっている＝ハイフンのぶんを切り詰められたということ
        // ホスト名が存在するときは、ユーザ名の後ろのハイフンを切り捨てられない。
        // （切り捨ててTextとして認識させたいが、ホスト名がある時にそれをやるとホスト名までText扱いになってしまうので）
        return failure()
      }

      val overallRange = startAt..hostnameResult.foundInfo.contentRange.last
      val contentRange = usernameResult.foundInfo.contentRange.first..hostnameResult.foundInfo.contentRange.last
      return success(
        FoundType.Mention,
        overallRange,
        contentRange,
        overallRange.next(),
        listOf(
          SubstringFoundInfo(
            FoundType.Mention,
            usernameResult.foundInfo.overallRange,
            usernameResult.foundInfo.contentRange,
            usernameResult.foundInfo.contentRange.next()
          ),
          SubstringFoundInfo(
            FoundType.Mention,
            hostnameResult.foundInfo.overallRange,
            hostnameResult.foundInfo.contentRange,
            hostnameResult.foundInfo.contentRange.next()
          )
        )
      )
    } else {
      val usernameResult = findUsernameRange(input, usernameRange)
      if (!usernameResult.success) {
        return failure()
      }

      val overallRange = startAt..usernameResult.foundInfo.contentRange.last
      val contentRange = usernameResult.foundInfo.contentRange
      return success(
        FoundType.Mention,
        overallRange,
        contentRange,
        overallRange.next(),
        listOf(
          SubstringFoundInfo(
            FoundType.Mention,
            contentRange,
            contentRange,
            contentRange.next()
          ),
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