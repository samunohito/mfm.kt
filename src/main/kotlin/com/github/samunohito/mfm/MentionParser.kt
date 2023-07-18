package com.github.samunohito.mfm

import com.github.samunohito.mfm.finder.MentionFinder
import com.github.samunohito.mfm.finder.SubstringFoundInfo
import com.github.samunohito.mfm.finder.core.FoundType
import com.github.samunohito.mfm.node.MfmMention

class MentionParser : SimpleParserBase<MfmMention>() {
  override val supportFoundTypes: Set<FoundType> = setOf(FoundType.Mention)

  private class MentionFormat(username: String, hostname: String?) {
    val username: String?
    val hostname: String?
    var invalid: Boolean = false
      private set

    init {
      val hostnameResult = parseHostname(hostname)
      if (hostnameResult.isSuccess) {
        this.hostname = hostnameResult.getOrThrow()
      } else {
        this.hostname = null
        this.invalid = true
      }

      val usernameResult = parseUsername(username)
      if (usernameResult.isSuccess) {
        val modifiedUsername = usernameResult.getOrThrow()
        if (username != modifiedUsername) {
          if (this.hostname == null) {
            this.username = modifiedUsername
          } else {
            this.username = null
            this.invalid = true
          }
        } else {
          this.username = modifiedUsername
        }
      } else {
        this.username = null
        this.invalid = true
      }
    }

    override fun toString(): String {
      if (this.invalid) {
        return ""
      }

      return if (this.hostname != null) {
        "@${this.username}@${this.hostname}"
      } else {
        "@${this.username}"
      }
    }

    companion object {
      private val regexDotHyphenTail = Regex("[.-]+$")
      private val regexDotHyphenHead = Regex("^[.-]")
      private val regexHyphenTail = Regex("-+$")

      fun parseUsername(username: String): Result<String> {
        // ハイフンで始まっている場合は、ユーザー名として認識しない
        if (username.startsWith("-")) {
          return Result.failure(IllegalArgumentException("Username must not start with a hyphen : $username"))
        }

        val hyphenTailMatch = regexHyphenTail.find(username)
          ?: return Result.success(username)

        // ハイフンで終わっている場合は、ユーザー名として認識しない
        val modifiedUsername = username.substring(0 until hyphenTailMatch.range.first)
        if (modifiedUsername.isEmpty()) {
          return Result.failure(IllegalArgumentException("Username must not end with a hyphen : $username"))
        }

        if (modifiedUsername.isEmpty()) {
          return Result.failure(IllegalArgumentException("Username cannot be blank."))
        }

        return Result.success(modifiedUsername)
      }

      fun parseHostname(hostname: String?): Result<String?> {
        if (hostname == null) {
          return Result.success(null)
        }

        // ドットまたはハイフンで始まっている場合は、ホスト名として認識しない
        if (regexDotHyphenHead.containsMatchIn(hostname)) {
          return Result.failure(IllegalArgumentException("Hostname must not start with a dot or hyphen : $hostname"))
        }

        val dotHyphenTailMatch = regexDotHyphenTail.find(hostname)
          ?: return Result.success(hostname)

        // ドットまたはハイフンで終わっている場合は、ホスト名として認識しない
        val modifiedHostname = hostname.substring(0 until dotHyphenTailMatch.range.first)
        if (modifiedHostname.isEmpty()) {
          return Result.failure(IllegalArgumentException("Hostname must not end with a dot or hyphen : $hostname"))
        }

        return Result.success(modifiedHostname)
      }
    }
  }

  override fun doParse(input: String, foundInfo: SubstringFoundInfo): IParserResult<MfmMention> {
    val usernameInfo = foundInfo[MentionFinder.SubIndex.Username]
    val hostnameInfo = foundInfo[MentionFinder.SubIndex.Hostname]

    val username = input.substring(usernameInfo.range)
    val hostname = if (hostnameInfo != SubstringFoundInfo.EMPTY) {
      input.substring(hostnameInfo.range)
    } else {
      null
    }

    val format = MentionFormat(username, hostname)
    if (format.invalid) {
      return failure()
    }

    return success(MfmMention(username, hostname, format.toString()), foundInfo)
  }
}