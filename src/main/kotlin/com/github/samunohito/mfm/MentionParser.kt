package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmMention

class MentionParser(private val context: Context = defaultContext) : IParser<MfmMention> {
  companion object {
    private const val regexGroupUserName = "username"
    private const val regexGroupHostName = "hostname"
    private val regexMention = Regex("^@(?<$regexGroupUserName>[a-zA-Z0-9_-]+)(@(?<$regexGroupHostName>[a-z0-9_.-]+))?")
    private val regexAlphaNumericTail = Regex("[0-9a-z]$", RegexOption.IGNORE_CASE)
    private val regexDotHyphenTail = Regex("[.-]+$")
    private val regexDotHyphenHead = Regex("^[.-]")
    private val regexHyphenTail = Regex("-+$")
    private val defaultContext = Context.init()

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
  }

  override fun parse(input: String, startAt: Int): ParserResult<MfmMention> {
    if (context.disabled) {
      return ParserResult.ofFailure()
    }

    val mentionMatch = regexMention.find(input, startAt)
      ?: return ParserResult.ofFailure()

    // メンションの直前が英数字だった場合、メンションとして認識しない
    val beforeStr = input.substring(0 until startAt)
    if (regexAlphaNumericTail.containsMatchIn(beforeStr)) {
      return ParserResult.ofFailure()
    }

    val username = mentionMatch.groups[regexGroupUserName]?.value ?: error("Regex name group not found")
    val hostname = mentionMatch.groups[regexGroupHostName]?.value
    val mention = MentionFormat(username, hostname)
    if (mention.invalid) {
      return ParserResult.ofFailure()
    }

    return ParserResult.ofSuccess(
      MfmMention(mention.username!!, mention.hostname, mention.toString()),
      mentionMatch.range,
      mentionMatch.range.last + 1,
    )
  }

  class Context private constructor(
    var disabled: Boolean
  ) {
    companion object {
      fun init(): Context {
        return Context(
          disabled = false
        )
      }
    }
  }
}