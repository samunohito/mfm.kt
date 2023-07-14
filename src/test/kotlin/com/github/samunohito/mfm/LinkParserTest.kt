package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmBold
import com.github.samunohito.mfm.node.MfmLink
import com.github.samunohito.mfm.node.MfmText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class LinkParserTest {
  private val parser = LinkParser()

  @Test
  fun basic() {
    val input = "[official instance](https://misskey.io/@ai)."
    val output = MfmLink(false, "https://misskey.io/@ai", listOf(MfmText("official instance")))
    assertEquals(output, parser.parse(input).node)
  }

  @Test
  fun `silent flag`() {
    val input = "?[official instance](https://misskey.io/@ai)."
    val output = MfmLink(true, "https://misskey.io/@ai", listOf(MfmText("official instance")))
    assertEquals(output, parser.parse(input).node)
  }

  @Test
  fun `with angle brackets url`() {
    val input = "[official instance](<https://misskey.io/@ai>)."
    val output = MfmLink(false, "https://misskey.io/@ai", listOf(MfmText("official instance")))
    assertEquals(output, parser.parse(input).node)
  }

  @Test
  fun `prevent xss`() {
    val input = "[official instance](javascript:foo)."
    assertFalse(parser.parse(input).success)
  }

  @Test
  fun `cannot nest a mention in a link label`() {
    val input = "[@example](<https://misskey.io/@ai>)."
    val output = MfmLink(false, "https://misskey.io/@ai", listOf(MfmText("@example")))
    assertEquals(output, parser.parse(input).node)
  }

  @Test
  fun `cannot nest a mention in a link label nested`() {
    val input = "[@example**@example**](<https://misskey.io/@ai>)."
    val output = MfmLink(
      false,
      "https://misskey.io/@ai",
      listOf(
        MfmText("@example"),
        MfmBold(listOf(MfmText("@example")))
      )
    )
    assertEquals(output, parser.parse(input).node)
  }
}