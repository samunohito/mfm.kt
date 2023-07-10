package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmMention
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class MentionParserTest {
  private val parser = MentionParser()

  @Test
  fun basic() {
    val username = "abc"
    val hostname = null
    val acct = acct(username, hostname)
    val input = "$acct"
    assertEquals(MfmMention(username, hostname, acct), parser.parse(input, 0).node)
  }

  @Test
  fun `basic remote`() {
    val username = "abc"
    val hostname = "misskey.io"
    val acct = acct(username, hostname)
    val input = "$acct"
    assertEquals(MfmMention(username, hostname, acct), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore format of mail address`() {
    val input = "abc@example.com"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `invalid char only username`() {
    val input = "@-"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `invalid char only hostname`() {
    val input = "@abc@."
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `allow '-' in username`() {
    val username = "abc-d"
    val hostname = null
    val acct = acct(username, hostname)
    val input = "$acct"
    assertEquals(MfmMention(username, hostname, acct), parser.parse(input, 0).node)
  }

  @Test
  fun `disallow '-' in head of username`() {
    val input = "@-abc"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `disallow '-' in tail of username`() {
    val input = "@abc-"
    assertEquals(MfmMention("abc", null, "@abc"), parser.parse(input, 0).node)
  }

  @Test
  fun `disallow period in head of hostname`() {
    val input = "@abc@.aaa"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `disallow period in tail of hostname`() {
    val input = "@abc@aaa."
    assertEquals(MfmMention("abc", "aaa", "@abc@aaa"), parser.parse(input, 0).node)
  }

  @Test
  fun `disallow '-' in head of hostname`() {
    val input = "@abc@-aaa"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `disallow '-' in tail of hostname`() {
    val input = "@abc@aaa-"
    assertEquals(MfmMention("abc", "aaa", "@abc@aaa"), parser.parse(input, 0).node)
  }

  private fun acct(username: String, hostname: String?): String {
    return "@$username" + (hostname?.let { "@$it" } ?: "")
  }

  private fun assertMention(expect: MfmMention, actual: MfmMention) {
    assertEquals(expect.props.username, actual.props.username)
    assertEquals(expect.props.host, actual.props.host)
    assertEquals(expect.props.acct, actual.props.acct)
  }
}